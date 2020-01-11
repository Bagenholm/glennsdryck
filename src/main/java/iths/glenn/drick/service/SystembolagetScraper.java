package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystembolagetScraper implements ScraperService{

    public DrinkStorage drinkStorage;
    public StoreStorage storeStorage;
    StoreEntity systembolaget;

    public SystembolagetScraper(DrinkStorage drinkStorage, StoreStorage storeStorage) {
        this.drinkStorage = drinkStorage;
        this.storeStorage = storeStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {
        Document doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
        Elements articles = doc.getElementsByTag("Artikel");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        systembolaget = storeStorage.findById("systembolaget")
                .orElse(new StoreEntity("systembolaget", "SEK"));

        articles.forEach(article -> drinks.add(makeDrink(article)));

        ArrayList<DrinkEntity> filteredDrinks =
        (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .filter(drinkEntity -> !Float.isNaN(drinkEntity.getAlcoholPerPrice()))
                .collect(Collectors.toList());

        systembolaget.setDrinks(filteredDrinks);
        filteredDrinks.forEach(drinkEntity ->
        {
            drinkStorage.save(drinkEntity);
        });

        return filteredDrinks;

        /* return drinkStorage.saveAll(
                drinks.stream()
                        .filter(drink -> !drink.getName().trim().isEmpty())
                        .filter(drink -> drink.getAlcoholPerPrice() != 0)
                        .collect(Collectors.toList())); */
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
        return article.getElementsByTag("Varugrupp").text();
    }

    private String extractNameFromText(Element article) {
        return (article.getElementsByTag("Namn").text().trim()
                    + " " + article.getElementsByTag("Namn2").text()).trim();
    }

}
