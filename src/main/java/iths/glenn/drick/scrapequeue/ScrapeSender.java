package iths.glenn.drick.scrapequeue;

import iths.glenn.drick.Scraper.ScraperService;
import iths.glenn.drick.Scraper.SystembolagetScraper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.core.Queue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Component
public class ScrapeSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

 /*   @Scheduled(fixedDelay = 86400000L * 2, initialDelay = 5000)
    public void send() {
        String message = "I'm scraping";
        template.convertAndSend(queue.getName(), message);
        System.out.println("Sent message: " + message);
    }
 */

    public void sendAllForScrape(ArrayList<ScraperService> scraperServiceList) {
        scraperServiceList.forEach(scraper -> {
            System.out.println("Sending to queue: " + scraper.getClass().getSimpleName());
            template.convertAndSend(queue.getName(), scraper);
        }) ;
    }

    public void sendScrapeToQueue(ScraperService scraperService) {
        System.out.println("Sending.. : " + scraperService.getClass().getSimpleName());
        template.convertAndSend(queue.getName(), scraperService.getClass().getSimpleName());
    }
}
