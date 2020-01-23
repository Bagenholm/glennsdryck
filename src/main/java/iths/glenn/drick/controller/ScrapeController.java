package iths.glenn.drick.controller;

import iths.glenn.drick.scraper.*;
import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.scrapequeue.ScrapeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/all")
    public ResponseEntity scrapeAll() {
        messageSender.sendScrapeToQueue(systembolagetScraper);
        messageSender.sendScrapeToQueue(calleScraper);
        messageSender.sendScrapeToQueue(fleggaardScraper);
        messageSender.sendScrapeToQueue(driveinbottleshopScraper);
        messageSender.sendScrapeToQueue(stenalineScraper);
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/systembolaget")
    public ResponseEntity scrapeSystembolaget() {
        try {
            return ResponseEntity.ok().body(systembolagetScraper.start());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/driveinbottleshop")
    public ResponseEntity scrapeDriveinbottleshop() {
        try {
            return ResponseEntity.ok().body(driveinbottleshopScraper.start());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/fleggaard")
    public ResponseEntity scrapeFleggaard() {
        try {
            return ResponseEntity.ok().body(fleggaardScraper.start());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/calle")
    public ResponseEntity scrapeCalle() {
        try {
            return ResponseEntity.ok().body(calleScraper.start());

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stenaline")
    public ResponseEntity scrapeStenaline() {
        try {
            return ResponseEntity.ok().body(stenalineScraper.start());

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
