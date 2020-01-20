package iths.glenn.drick.Scraper;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
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
public class FleggaardScraper implements ScraperService {

    @Autowired
    DrinkStorage drinkStorage;
    @Autowired
    StoreStorage storeStorage;
    StoreEntity fleggaard;

    BrowserEngine browser;
    float currencyExchangeRate;

    Logger logger = LoggerFactory.getLogger(FleggaardScraper.class);

    @Override
    public List<DrinkEntity> start() throws IOException {
        fleggaard = getStore();

        if(fleggaard.isScrapedRecently()) {
            logger.info("Fleggaard scraped recently. Fetching from DB.");
            return drinkStorage.findByStore("fleggaard");
        }
        return scrape();
    }

    public StoreEntity getStore() {
        return storeStorage.findById("fleggaard")
                .orElse(new StoreEntity("fleggaard", "DKK", "puttgarden"));
    }

    public List<DrinkEntity> scrape() throws IOException {
        fleggaard = getStore();
        currencyExchangeRate = CurrencyExchangeRateService.exchangeRate(fleggaard.getCurrency());

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();

        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .collect(Collectors.toList());

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));
        fleggaard.setInstanceLastScrapedToNow();
        storeStorage.save(fleggaard);

        return filteredDrinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        browser = BrowserFactory.getWebKit(); // Throws NPE if run on JDK11 without JavaFX added as a library
        Page page = browser.navigate(url);
        String htmlString = page.getDocument().queryAll(".products").toString();
        Document doc = Jsoup.parse(htmlString);
        Elements articles = doc.getElementsByClass("product");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> {
            drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        try {
            String name = extractNameFromText(article);
            float alcohol = extractAlcoholFromText(article);
            float volume = extractVolumeFromText(article) * 1000; //Product volume comes in litres
            float price = extractPriceFromText(article) * currencyExchangeRate;
            float pricePerLitre = 1000 / volume * price;
            return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, fleggaard);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnreadableProductException("Unreadable product from " + article);
        }

    }

    private float extractPriceFromText(Element article) {
        String priceTagText = article.getElementsByClass("productOnePrice").get(0).text();
        int dkkIndex = priceTagText.indexOf("DKK ");
        int sekIndex = priceTagText.indexOf("SEK ");

        String priceString = priceTagText.substring(dkkIndex + sekIndex + 5); //One of the indexes will be -1, allows both SEK and DKK parsing

        priceString = priceString.replace(".", "").replace(",", ".");

        float returnNumber = 0f;
        try {returnNumber = Float.parseFloat(priceString);
        } catch (NumberFormatException e){
            System.err.println(priceTagText + " " + priceString);
        }

        return returnNumber;
    }

    private String extractNameFromText(Element article) {
        String nameString = article.getElementsByClass("productname").text();
        int percIndex = nameString.indexOf("%");
        int commaIndex = nameString.indexOf(",");
        if(percIndex > 0 && percIndex > commaIndex) {
            return nameString.substring(0, percIndex - 4).trim();
        } else if(percIndex > 0 && commaIndex > percIndex) {
            return nameString.substring(0, commaIndex - 6).trim();
        } else if(percIndex == -1 && commaIndex > 0) {
            return nameString.substring(0, commaIndex - 4);
        }

        return "";
    }

    private float extractAlcoholFromText(Element article) {
        String alkoholhalt = article.getElementsByClass("productname").text();
        int percIndex = alkoholhalt.indexOf("%");
        int minIndex = percIndex - 4;
        if(minIndex >= 0) {
            String substring = alkoholhalt.substring(minIndex, percIndex)
                    .trim()
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö %\"]", "")
                    .replace("-", "")
                    .replaceAll(",", ".");
            float alcohol = Float.parseFloat(substring);
            if(alcohol < 100) {
                return Float.parseFloat(substring);
            }
        }
        return 0f;
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByClass("productname").text();
        int percIndex = volumeString.indexOf("%");
        String substring = volumeString.substring(percIndex + 1);
        int xIndex = substring.indexOf("x");
        int lIndex = substring.lastIndexOf("l");
        float packMultiplier = 1f;
        if(percIndex >= 0) {
            if(xIndex > 0) {
                packMultiplier = multiPackMultiplier(substring.substring(0, xIndex + 1), substring);
            } else {
                packMultiplier = multiPackMultiplier("1", substring);
            }
        } else if (percIndex == -1) {
            return 0f;
        }
        if(xIndex >= 0 && lIndex > 0) {
            substring = substring.substring(xIndex, lIndex)
                    .trim()
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö %]", "")
                    .replaceAll("[,]", ".");
            if(!substring.isEmpty()) {
                return Float.parseFloat(substring) * packMultiplier;
            } else if (volumeString.contains("33 xl")) {
                return Float.parseFloat("33") * packMultiplier / 100;
            }
        } if(xIndex == -1 && lIndex >= 0) {
            substring = substring.substring(0, lIndex)
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö. ]", "")
                    .replaceAll(",", ".");
            if(substring.startsWith("0") && substring.charAt(1) != '.') {  //Prevents reading article with volume 0.7 as 07, making it 7 instead of 0.7.
                substring = substring.replaceFirst("0", "0.");
            }
            return Float.parseFloat(substring) * packMultiplier;
        }

        return 0f;
    }

    private float multiPackMultiplier(String multiString, String multipackStringWithVolume) {
        int lIndex = multipackStringWithVolume.lastIndexOf('l');
        char volumePrefix = ' ';
        if(lIndex > 0) {
            volumePrefix = multipackStringWithVolume.charAt(lIndex - 1);
        }
        if(multipackStringWithVolume.contains("4-pack")) {
            return 20f / 100; //Odd case where they write 4-pack on a single product.
        }
        if(volumePrefix == 'c') {
            return Float.parseFloat(multiString.replaceAll("[a-zA-Z]", "").trim()) / 100;
        } else if(volumePrefix == 'm') {
            return Float.parseFloat(multiString.replaceAll("[a-zA-Z]", "").trim()) / 1000;
        } else if(multiString.contains("x")) {
            return Float.parseFloat(multiString.replaceAll("x", "").trim());
        }
        return 1f;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Öl", "Ale", "https://www.fleggaard.dk/pl/%C3%96l-Ale_40381.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Klassisk", "https://www.fleggaard.dk/pl/%C3%96l-Klassik_40376.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Guld", "https://www.fleggaard.dk/pl/%C3%96l-Guld%C3%B6l_40377.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "https://www.fleggaard.dk/pl/%C3%96l-Vete%C3%B6l_40384.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Lager", "https://www.fleggaard.dk/pl/%C3%96l-Lager_40383.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Pilsner", "https://www.fleggaard.dk/pl/%C3%96l-Pilsner_40375.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Stout", "https://www.fleggaard.dk/pl/%C3%96l-Porter---Stout_40382.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Special", "https://www.fleggaard.dk/pl/%C3%96l-Special%C3%B6l_57644.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Svensk", "https://www.fleggaard.dk/pl/%C3%96l-Svensk-%C3%B6l_40379.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Cider och alkoläsk", "Cider", "https://www.fleggaard.dk/pl/Cider_40514.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Vitt vin", "https://www.fleggaard.dk/pl/Vin-Hvidvin_40408.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "https://www.fleggaard.dk/pl/Vin-R%C3%B8dvin_40407.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Rosévin", "https://www.fleggaard.dk/pl/Vin-Ros%C3%A9vin_287395.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Champagne", "https://www.fleggaard.dk/pl/Vin-Champagne_287401.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande", "https://www.fleggaard.dk/pl/Vin-Mousserende_287398.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Dessertvin", "https://www.fleggaard.dk/pl/Vin-Dessertvin_287406.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Portvin", "https://www.fleggaard.dk/pl/Vin-Portvin_287410.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Absint", "https://www.fleggaard.dk/pl/Sprit-Absint_40392.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Akvavit", "https://www.fleggaard.dk/pl/Sprit-Akvavit---Snaps_40393.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Bitter", "https://www.fleggaard.dk/pl/Sprit-Bitter_40394.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Brandy", "https://www.fleggaard.dk/pl/Sprit-Brandy_40396.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Calvados", "https://www.fleggaard.dk/pl/Sprit-Calvados_40397.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Cognac", "https://www.fleggaard.dk/pl/Sprit-Cognac_40398.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Creme likör", "https://www.fleggaard.dk/pl/Sprit-Creme-Lik%C3%B6r_287403.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Brännvin", "https://www.fleggaard.dk/pl/Sprit-Olika-Br%C3%A4nnvin_287405.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Drinkmix", "https://www.fleggaard.dk/pl/Sprit-Drink-Mix_287407.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Gin", "https://www.fleggaard.dk/pl/Sprit-Gin_40399.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Glögg", "https://www.fleggaard.dk/pl/Sprit-Gl%C3%B6gg_287409.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Grappa", "https://www.fleggaard.dk/pl/Sprit-Grappa_40400.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "https://www.fleggaard.dk/pl/Sprit-Lik%C3%B6r_40395.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Ready to drink", "https://www.fleggaard.dk/pl/Sprit-Ready-to-Drink_40517.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "https://www.fleggaard.dk/pl/Sprit-Rom_40401.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Sherry", "https://www.fleggaard.dk/pl/Sprit-Sherry_287412.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Shots", "https://www.fleggaard.dk/pl/Sprit-Shots_40402.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Tequila", "https://www.fleggaard.dk/pl/Sprit-Tequila_40403.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Vermouth", "https://www.fleggaard.dk/pl/Sprit-Vermouth_287413.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Vodka", "https://www.fleggaard.dk/pl/Sprit-Vodka_40404.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "https://www.fleggaard.dk/pl/Sprit-Whisky_40405.aspx?locId=732"));

        return drinks;
    }
}
