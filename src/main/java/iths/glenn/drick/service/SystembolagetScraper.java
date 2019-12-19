package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.model.DrinkModel;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public List<DrinkEntity> scrape(String url) {
        ArrayList<DrinkEntity> drinksList = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements articles = doc.getElementsByTag("Artikel");


        doc.getElementsByTag("Namn").forEach(System.out::println);

        return Collections.emptyList();
    }

}
