package iths.glenn.drick.controller;

import iths.glenn.drick.Scraper.*;
import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.scrapequeue.ScrapeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/scrape")
public class ScrapeController {

    @Autowired
    public SystembolagetScraper systembolagetScraper;
    @Autowired
    public DriveinbottleshopScraper driveinbottleshopScraper;
    @Autowired
    public FleggaardScraper fleggaardScraper;
    @Autowired
    public CalleScraper calleScraper;
    @Autowired
    public StenalineScraper stenalineScraper;

    @Autowired
    ScrapeSender messageSender;

    //@Scheduled(fixedDelay = 172800000L, initialDelay = 10000L)
    @GetMapping("/all")
    public void scrapeAll() {
        messageSender.sendScrapeToQueue(systembolagetScraper);
        messageSender.sendScrapeToQueue(calleScraper);
        messageSender.sendScrapeToQueue(fleggaardScraper);
        messageSender.sendScrapeToQueue(driveinbottleshopScraper);
        messageSender.sendScrapeToQueue(stenalineScraper);
    }


    @GetMapping("/systembolaget")
    public List<DrinkEntity> scrapeSystembolaget() {
        try {
            return systembolagetScraper.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/driveinbottleshop")
    public List<DrinkEntity> scrapeDriveinbottleshop() {
        try {
            return driveinbottleshopScraper.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/fleggaard")
    public List<DrinkEntity> scrapeFleggaard() {
        try {
            return fleggaardScraper.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/calle")
    public List<DrinkEntity> scrapeCalle() {
        try {
            return calleScraper.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @GetMapping("/stenaline")
    public List<DrinkEntity> scrapeStenaline() {
        try {
            return stenalineScraper.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
