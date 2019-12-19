package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.model.DrinkModel;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystembolagetScraper implements ScraperService{

    @Override
    public List<DrinkEntity> scrape() throws IOException {
        ArrayList<DrinkEntity> drinksList = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
        Elements articles = doc.getElementsByTag("Artikel");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.stream()
                //.filter(article -> article.getElementsByTag("Utgått").equals("0"))
                .forEach(article -> drinks.add(makeDrink(article)));

        return drinks;
    }

    public DrinkEntity makeDrink(Element article) {
        String name = article.getElementsByTag("Namn").text()
                + " " + article.getElementsByTag("Namn2").text();
        String type = article.getElementsByTag("Varugrupp").text();
        String subtype = article.getElementsByTag("Typ").text();
        float pricePerLitre = Float.parseFloat(article.getElementsByTag("PrisPerLiter").text());
        String alcoholString = article.getElementsByTag("Alkoholhalt").text();
        float alcohol = Float.parseFloat(alcoholString.substring(0, (alcoholString.length() - 1)));
        return new DrinkEntity(name, type, subtype, pricePerLitre, alcohol);
    }

}
