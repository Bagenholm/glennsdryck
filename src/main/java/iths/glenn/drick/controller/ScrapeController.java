package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.service.DriveinbottleshopScraper;
import iths.glenn.drick.service.FleggaardScraper;
import iths.glenn.drick.service.SystembolagetScraper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/scrape")
public class ScrapeController {

    SystembolagetScraper systembolagetScraper;
    DriveinbottleshopScraper driveinbottleshopScraper;
    FleggaardScraper fleggaardScraper;

    public ScrapeController(SystembolagetScraper systembolagetScraper, DriveinbottleshopScraper driveinbottleshopScraper, FleggaardScraper fleggaardScraper) {
        this.systembolagetScraper = systembolagetScraper;
        this.driveinbottleshopScraper = driveinbottleshopScraper;
        this.fleggaardScraper = fleggaardScraper;
    }

    /* StoreStorage storeStorage;
    ShopScrapeService scraper;

    public ScrapeController(StoreStorage storeStorage, ShopScrapeService scraper) {
        this.storeStorage = storeStorage;
        this.scraper = scraper;
    } */

    @GetMapping("/systembolaget")
    public List<DrinkEntity> scrapeAll() {
        try {
            return systembolagetScraper.scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/driveinbottleshop")
    public List<DrinkEntity> scrapeDriveinbottleshop() {
        try {
            return driveinbottleshopScraper.scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/fleggaard")
    public List<DrinkEntity> scrapeFleggaard() {
        try {
            return fleggaardScraper.scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
