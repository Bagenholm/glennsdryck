package iths.glenn.drick.Scraper;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.exception.UnreadableProductException;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.repository.StoreStorage;
import iths.glenn.drick.service.CurrencyExchangeRateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalleScraper implements ScraperService{

    @Autowired
    DrinkStorage drinkStorage;
    @Autowired
    StoreStorage storeStorage;
    StoreEntity calle;

    float currencyExchangeRate;
    Logger logger = LoggerFactory.getLogger(CalleScraper.class);

    @Override
    public List<DrinkEntity> start() throws IOException {
        calle = getStore();

        if(calle.isScrapedRecently()) {
            logger.info("Calle scraped recently. Fetching from DB.");
            return drinkStorage.findByStore("calle");
        }
        return scrape();
    }

    public StoreEntity getStore() {
        return storeStorage.findById("calle")
                .orElse(new StoreEntity("calle", "EUR"));
    }

    public List<DrinkEntity> scrape() throws IOException {
        calle = getStore();

        currencyExchangeRate = CurrencyExchangeRateService.exchangeRate(calle.getCurrency());

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();

        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .collect(Collectors.toList());

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));
        calle.setInstanceLastScrapedToNow();
        storeStorage.save(calle);

        return filteredDrinks;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "https://calle.ee/?id=37&cat=27"));
        drinks.addAll(scrapeDrinks("Vin", "Vitt vin", "https://calle.ee/?id=37&cat=28"));
        drinks.addAll(scrapeDrinks("Vin", "Rosévin", "https://calle.ee/?id=37&cat=29"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande vin", "https://calle.ee/?id=37&cat=30"));
        drinks.addAll(scrapeDrinks("Vin", "Övrigt", "https://calle.ee/?id=37&cat=32"));
        drinks.addAll(scrapeDrinks("Vin", "BIB", "https://calle.ee/?id=37&cat=33"));
        drinks.addAll(scrapeDrinks("Öl", "Öl", "https://calle.ee/?id=37&cat=4"));
        drinks.addAll(scrapeDrinks("Sprit", "Vodka", "https://calle.ee/?id=37&cat=13"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "https://calle.ee/?id=37&cat=14"));
        drinks.addAll(scrapeDrinks("Sprit", "Cognac", "https://calle.ee/?id=37&cat=15"));
        drinks.addAll(scrapeDrinks("Sprit", "Brandy", "https://calle.ee/?id=37&cat=16"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "https://calle.ee/?id=37&cat=17"));
        drinks.addAll(scrapeDrinks("Sprit", "Bitter", "https://calle.ee/?id=37&cat=18"));
        drinks.addAll(scrapeDrinks("Sprit", "Gin", "https://calle.ee/?id=37&cat=19"));
        drinks.addAll(scrapeDrinks("Sprit", "Snaps", "https://calle.ee/?id=37&cat=20"));
        drinks.addAll(scrapeDrinks("Sprit", "Calvados", "https://calle.ee/?id=37&cat=22"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "https://calle.ee/?id=37&cat=23"));
        drinks.addAll(scrapeDrinks("Sprit", "Tequila", "https://calle.ee/?id=37&cat=24"));
        drinks.addAll(scrapeDrinks("Sprit", "Annan starksprit", "https://calle.ee/?id=37&cat=26"));
        drinks.addAll(scrapeDrinks("Sprit", "Vermouth", "https://calle.ee/?id=37&cat=25"));
        drinks.addAll(scrapeDrinks("Sprit", "Portvin", "https://calle.ee/?id=37&cat=31"));
        drinks.addAll(scrapeDrinks("Sprit", "Glögg", "https://calle.ee/?id=37&cat=40"));
        drinks.addAll(scrapeDrinks("Cider och alkoläsk", "Cider", "https://calle.ee/?id=37&cat=5"));
        drinks.addAll(scrapeDrinks("Cider och alkoläsk", "Alkoläsk", "https://calle.ee/?id=37&cat=6"));

        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements articles = doc.getElementsByClass("list-item");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> drinks.add(makeDrink(article, type, subtype)));

        return drinks;
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        String name = extractNameFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article);
        float price = extractPriceFromText(article) * currencyExchangeRate;

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, calle);
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByTag("h1").text();
        int lIndex = volumeString.lastIndexOf('l');
        if(lIndex == -1) {
            return 0f;
        }
        char volumePrefix = volumeString.charAt(lIndex - 1);
        if(!String.valueOf(volumePrefix).matches("[cm ]")) {  //If lIndex is not the last l in volumeString the volume, i. e. 'cl', 'ml' or ' l'.
            lIndex = volumeString.substring(0, lIndex).lastIndexOf('l'); // Second from last 'l'.
        }
        int spaceBeforeVolumeIndex = volumeString.substring(0, lIndex).lastIndexOf(' ');
        int multiPackMultiplier = 1;
        if(isUsualDescription(spaceBeforeVolumeIndex, lIndex)) {
            String packAndVolumeString = volumeString.substring(spaceBeforeVolumeIndex, lIndex);
            multiPackMultiplier = extractMultiPackMultiplier(packAndVolumeString);
        } else {
            volumeString = volumeString.substring(0, lIndex);
            lIndex = volumeString.lastIndexOf('l');
        }
        int volumeMultiplier = volumePrefixMultiplier(volumeString, volumePrefix) * multiPackMultiplier;
        if(multiPackMultiplier > 1) {
            volumeString = volumeString.substring(volumeString.lastIndexOf('x'), lIndex); //Prevents 24x33 to be read as 2433
        } else {
            volumeString = volumeString.substring(spaceBeforeVolumeIndex, lIndex);
        }
        volumeString = volumeString
                .replaceAll("[a-öA-Ö %]", "")
                .replace(",", ".");
        if(volumeString.matches("[\\d]+")) {
            try {
                return Float.parseFloat(volumeString) * volumeMultiplier;
            } catch(NumberFormatException e) {
                throw new UnreadableProductException("Unable to parse volume from: [" + volumeString + "]");
            }
        }
        return 0f;
    }

    private int extractMultiPackMultiplier(String volumeString) {
        int packMultiplier = 1;
        if(volumeString.contains("x")) {
            String multiPackString = volumeString.substring(0, volumeString.indexOf('x')).trim();
            packMultiplier = Integer.parseInt(multiPackString);
        }
        return packMultiplier;
    }
    private int volumePrefixMultiplier(String volumeString, char volumePrefix) {
        if(volumePrefix == 'c') {
            return 10;
        } else if(volumePrefix == 'm') {
            return 1;
        } else if(String.valueOf(volumePrefix).matches("[0-9]")) {
            return 1000;
        }
        return 1;
    }

    private float extractAlcoholFromText(Element article) {
        String alcoholString = article.getElementsByTag("h1").text();
        int percIndex = alcoholString.indexOf('%');
        int lIndex = alcoholString.lastIndexOf('l');
        if(isUsualDescription(lIndex, percIndex)) {
            String alcoholSubString = alcoholString.substring(lIndex + 1, percIndex).trim();
            alcoholSubString = alcoholSubString.replace(",", ".").replaceAll("[a-zA-Z ]", "");

            try {
                return Float.parseFloat(alcoholSubString);
            } catch (NumberFormatException e) {
                throw new UnreadableProductException("Unable to alcohol content from: [" + alcoholString + "]");
            }
        } else {
            try {
                return extractAlcoholFromOddCaseText(percIndex, lIndex, alcoholString);
            } catch (NumberFormatException e) {
                throw new UnreadableProductException("Unable to alcohol content from: [" + alcoholString + "]");
            }
        }
    }

    private float extractAlcoholFromOddCaseText(int percIndex, int lIndex, String alcoholString) {
        if(alcoholString.contains("klaasi")) {
            lIndex = alcoholString.substring(0, alcoholString.indexOf('+')).lastIndexOf('l');
            String alcoholSubString = alcoholString.substring(lIndex + 1, percIndex).trim();
            alcoholSubString = alcoholSubString.replace(",", ".").replaceAll("[a-zA-Z ]", "");
            return Float.parseFloat(alcoholSubString);
        }
        return 0;
    }

    private boolean isUsualDescription(int lowerIndex, int higherIndex ) {
        return (higherIndex > 0 && lowerIndex> 0 && higherIndex > lowerIndex);
    }

    private String extractNameFromText(Element article) {
        String nameString = article.getElementsByTag("h1").text();
        int lIndex = nameString.lastIndexOf('l');
        if(lIndex == -1) {
            return "";
        }
        int spaceBeforeVolume = nameString.substring(0, lIndex).lastIndexOf(' ');
        return nameString.substring(0, spaceBeforeVolume);
    }

    private float extractPriceFromText(Element article) {
        String priceString = article.getElementsByClass("hind").text();
        try {
            return Float.parseFloat(priceString);
        } catch (NumberFormatException e) {
            throw new UnreadableProductException("Unable to alcohol content from: [" + priceString + "]");
        }
    }


}
