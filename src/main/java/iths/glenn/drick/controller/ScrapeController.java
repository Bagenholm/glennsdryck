package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrape")
public class ScrapeController {

    /* StoreStorage storeStorage;
    ShopScrapeService scraper;

    public ScrapeController(StoreStorage storeStorage, ShopScrapeService scraper) {
        this.storeStorage = storeStorage;
        this.scraper = scraper;
    } */

    @GetMapping("/all")
    public DrinkEntity scrapeAll() {
        return new DrinkEntity("Test");
    }
}
