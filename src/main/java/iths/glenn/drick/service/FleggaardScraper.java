package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FleggaardScraper implements ScraperService {

    DrinkStorage drinkStorage;

    public FleggaardScraper(DrinkStorage drinkStorage) {
        this.drinkStorage = drinkStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {

        Document doc;
        doc = Jsoup.connect("https://www.fleggaard.dk/info/LandingPages_5334l4.aspx?locId=732&uid=0.41575743203312254#showsortimentSelect").get();

        Elements articles = doc.getElementsByClass("product");

        System.err.println(doc.toString());

        System.out.println(articles);



        return Collections.emptyList();
    }


    private ArrayList<DrinkEntity> scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("");

        Elements articles = doc.getElementsByClass("plist");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        articles.forEach(article -> {
            System.err.println(article.getElementsByTag("p").text() + "\n" +
                    "Name: " + extractNameFromText(article) + "\n" +
                    "Price: " + extractPriceFromText(article) + "\n" +
                    "Alcohol: " + extractAlcoholFromText(article) + "\n" +
                    "Volume: " + extractVolumeFromText(article) );
            drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;
    }

    private void getElementsByTextForHtmlParse(String s) throws IOException {
        Document doc;
        doc = Jsoup.connect(s).get();
        Elements articles = doc.getElementsByClass("product-search");

        System.out.println(articles);
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        String name = extractNameFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article) * 10;
        float price = extractPriceFromText(article);

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume);
    }

    private float extractPriceFromText(Element article) {
        return 0f;
    }

    private String extractNameFromText(Element article) {
        return "";
    }

    private float extractAlcoholFromText(Element article) {
        return 0f;
    }

    private float extractVolumeFromText(Element article) {
        return 0f;
    }

    public float extractVolumeFromTextOddCases(String name, String volumeString) {
        return 0f;
    }

    private int multiPackMultiplier() {
        return 0;
    }

    private boolean isMultiPack() {


        return false;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();



        return drinks;
    }
}
