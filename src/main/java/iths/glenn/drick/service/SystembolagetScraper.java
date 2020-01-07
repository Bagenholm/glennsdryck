package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.model.DrinkModel;
import iths.glenn.drick.repository.DrinkStorage;
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

    public SystembolagetScraper(DrinkStorage drinkStorage) {
        this.drinkStorage = drinkStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {
        Document doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
        Elements articles = doc.getElementsByTag("Artikel");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.stream().forEach(article -> drinks.add(makeDrink(article)));

        return drinkStorage.saveAll(
                drinks.stream()
                        .filter(drink -> drink.getName().trim().isEmpty())
                        .filter(drink -> drink.getAlcoholPerPrice() != 0)
                        .collect(Collectors.toList()));
    }

    public DrinkEntity makeDrink(Element article) {
        String name = extractNameFromText(article);
        String type = extractTypeFromText(article);
        String subtype = extractSubtypeFromText(article);
        float price = extractPriceFromText(article);
        float pricePerLitre = extractPricePerLitreFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article);

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume);
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
