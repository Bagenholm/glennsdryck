package iths.glenn.drick.scraper;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystembolagetScraper implements ScraperService {
    static final long serialVersionUID = 1L;

    @Autowired
    DrinkStorage drinkStorage;
    @Autowired
    StoreStorage storeStorage;

    StoreEntity systembolaget;

    Logger logger = LoggerFactory.getLogger(SystembolagetScraper.class);

    @Override
    public List<DrinkEntity> start() throws IOException {
        systembolaget = getStore();

        if (systembolaget.isScrapedRecently()) {
            logger.info("Systembolaget scraped recently. Fetching from DB.");
            return drinkStorage.findByStore(systembolaget.getStoreName());
        }
        return scrape();
    }

    public StoreEntity getStore() {
        return storeStorage.findById("systembolaget")
                .orElse(new StoreEntity("systembolaget", "SEK", "göteborg"));
    }

    public List<DrinkEntity> scrape() throws IOException {
        systembolaget = getStore();
        Document doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
        Elements articles = doc.getElementsByTag("Artikel");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> drinks.add(makeDrink(article)));

        ArrayList<DrinkEntity> filteredDrinks =
                (ArrayList<DrinkEntity>) drinks.stream()
                        .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                        .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                        .filter(drinkEntity -> !Float.isNaN(drinkEntity.getAlcoholPerPrice()))
                        .collect(Collectors.toList());

        filteredDrinks.forEach(drinkEntity ->  drinkStorage.save(drinkEntity));
        systembolaget.setInstanceLastScrapedToNow();
        storeStorage.save(systembolaget);


        return filteredDrinks;
    }

    public DrinkEntity makeDrink(Element article) {
        String name = extractNameFromText(article);
        String type = extractTypeFromText(article);
        String subtype = extractSubtypeFromText(article);
        float price = extractPriceFromText(article);
        float pricePerLitre = extractPricePerLitreFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article);

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, systembolaget);
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByTag("Volymiml").text();
        if(volumeString.isEmpty()) {
            return 0f;
        }
        return Float.parseFloat(volumeString);
    }

    private float extractAlcoholFromText(Element article) {
        String alcoholString = article.getElementsByTag("Alkoholhalt").text();
        if(alcoholString.isEmpty()) {
            return 0f;
        }
        return Float.parseFloat(alcoholString.substring(0, (alcoholString.length() - 1)));
    }

    private float extractPricePerLitreFromText(Element article) {
        String priceString = article.getElementsByTag("PrisPerLiter").text();
        if(priceString.isEmpty()) {
            return 0f;
        }
        return Float.parseFloat(priceString);
    }

    private float extractPriceFromText(Element article) {
        String priceString = article.getElementsByTag("Prisinklmoms").text();
        if(priceString.isEmpty()) {
            return 0f;
        }
        return Float.parseFloat(article.getElementsByTag("Prisinklmoms").text());
    }

    private String extractSubtypeFromText(Element article) {
        return article.getElementsByTag("Typ").text();
    }

    private String extractTypeFromText(Element article) {
        String typeText = article.getElementsByTag("Varugrupp").text();
        if(typeText.contains("vin")) {
            typeText = "Vin";
        }
        return typeText;
    }

    private String extractNameFromText(Element article) {
        return (article.getElementsByTag("Namn").text().trim()
                    + " " + article.getElementsByTag("Namn2").text()).trim();
    }
}
