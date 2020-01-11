package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.service.CalleScraper;
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
    CalleScraper calleScraper;

    public ScrapeController(SystembolagetScraper systembolagetScraper, DriveinbottleshopScraper driveinbottleshopScraper, FleggaardScraper fleggaardScraper, CalleScraper calleScraper) {
        this.systembolagetScraper = systembolagetScraper;
        this.driveinbottleshopScraper = driveinbottleshopScraper;
        this.fleggaardScraper = fleggaardScraper;
        this.calleScraper = calleScraper;
    }

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

    @GetMapping("/calle")
    public List<DrinkEntity> scrapeCalle() {
        try {
            return calleScraper.scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
