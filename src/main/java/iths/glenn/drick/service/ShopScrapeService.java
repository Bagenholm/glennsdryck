package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ShopScrapeService {
    public void scrapeAll() {
        Document doc;
        doc = Jsoup.connect("https://www.systembolaget.se/api/assortment/products/xml").get();
    }



/*    StoreStorage storeStorage;

    public ShopScrapeService(StoreStorage storeStorage) {
        this.storeStorage = storeStorage;
    } */
}
