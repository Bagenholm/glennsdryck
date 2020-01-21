package iths.glenn.drick.scrapequeue;

import iths.glenn.drick.controller.ScrapeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScrapeReceiver {

    Logger logger = LoggerFactory.getLogger(ScrapeReceiver.class);

    @Autowired
    ScrapeController scrapeController;

    @RabbitListener(queues = "scrape")
    public void receiveScrape(String message) throws IOException {
   /*     switch(message) {
            case("SystembolagetScraper"):
                logger.info("Scraping with" + message);
                scrapeController.systembolagetScraper.scrape();
                break;
            case("DriveinbottleshopScraper"):
                logger.info("Scraping with" + message);
                scrapeController.driveinbottleshopScraper.scrape();
                break;
            case("FleggaardScraper"):
                logger.info("Scraping with" + message);
                scrapeController.fleggaardScraper.scrape();
                break;
            case("CalleScraper"):
                logger.info("Scraping with" + message);
                scrapeController.calleScraper.scrape();
                break;
            case("StenalineScraper"):
                logger.info("Scraping with" + message);
                scrapeController.stenalineScraper.scrape();
                break;
        }*/
    }
}
