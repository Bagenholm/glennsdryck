package iths.glenn.drick.controller;

import iths.glenn.drick.service.SystembolagetScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/scrape")
public class ScrapeController {

    SystembolagetScraper shopScraper;

    public ScrapeController(SystembolagetScraper shopScraper) {
        this.shopScraper = shopScraper;
    }

    /* StoreStorage storeStorage;
    ShopScrapeService scraper;

    public ScrapeController(StoreStorage storeStorage, ShopScrapeService scraper) {
        this.storeStorage = storeStorage;
        this.scraper = scraper;
    } */

    @GetMapping("/all")
    public void scrapeAll() {
        try {
            shopScraper.scrapeSystembolaget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
