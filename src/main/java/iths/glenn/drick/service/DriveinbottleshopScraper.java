package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DriveinbottleshopScraper implements ScraperService {
    @Override
    public List<DrinkEntity> scrape() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        Document lightBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/ljus-ol/").get();
//        Document wheatBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/veteol/").get();
//        Document darkBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/mork-ol/").get();

        Elements articles = lightBeer.getElementsByClass("product-search");

        articles.forEach(article -> drinks.add(makeDrink(article)));



        //drinks.forEach(System.out::println);
        return drinks;
    }

    private DrinkEntity makeDrink(Element article) {
        String name = article.getElementsByTag("h3").text();


        return new DrinkEntity("", "", "", 44.4f, 44.4f);
    }


}
