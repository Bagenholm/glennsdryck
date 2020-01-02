package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DriveinbottleshopScraper implements ScraperService {
    @Override
    public List<DrinkEntity> scrape() throws IOException {

        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        drinks.addAll(scrapeLjusOl());
        drinks.addAll(scrapeWheatBeer());
        drinks.addAll(scrapeDarkBeer());


        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDarkBeer() throws IOException {
        Document doc = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/mork-ol/").get();
        Elements articles = doc.getElementsByClass("product-search");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

         articles.forEach(article -> {
            drinks.add(makeDrink(article, "Öl", "Mörk öl"));
        });
        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeWheatBeer() throws IOException {
        Document doc = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/veteol/").get();
        Elements articles = doc.getElementsByClass("product-search");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> {
            drinks.add(makeDrink(article, "Öl", "Veteöl"));
        });
        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeLjusOl() throws IOException {
        Document doc = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/ljus-ol/").get();
        Elements articles = doc.getElementsByClass("product-search");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> {
            drinks.add(makeDrink(article, "Öl", "Ljus öl"));
        });
        return drinks;
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        String name = article.getElementsByTag("h3").text();
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article) * 10;
        float price = extractPriceFromText(article);

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume);
    }

    private float extractPriceFromText(Element article) {
        String priceString = article.getElementsByTag("h4").text();
        return Float.parseFloat(priceString.replaceAll("[a-öA-Ö, ]", ""));
    }

    private float extractAlcoholFromText(Element article) {
        String alkoholhalt = article.getElementsByTag("p").text();
        int percIndex = alkoholhalt.indexOf("%");
        int minIndex = percIndex-6;
        if(minIndex >= 0) {
            String substring = alkoholhalt.substring(minIndex, percIndex + 1)
                    .replaceAll("[a-zA-Z %]", "")
                    .replaceAll("^[,|.]", "")
                    .replace(",", ".");
            return Float.parseFloat(substring);
        }
        return 0f;
    }

    private float extractVolumeFromText(Element article) {
        String volume = article.getElementsByTag("p").text();
        int clCommaIndex = volume.indexOf("cl,");
        int minIndex = clCommaIndex - 6;
        if(minIndex >= 0) {
            String substring = volume.substring(minIndex, clCommaIndex);
            if(substring.contains("x")) {
                if(substring.contains("24")) {
                    return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", "")) * 24;
                }
            }
            return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", ""));
        } else if(minIndex == -7) {
            int clPeriodIndex = volume.indexOf("cl.");
            minIndex = clPeriodIndex - 6;
            if(minIndex >= 0) {
                String substring = volume.substring(minIndex, clPeriodIndex);
                return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", ""));
            }
        }
        return 0f;
    }

}