package iths.glenn.drick.scrapequeue;

import iths.glenn.drick.scraper.ScraperService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ScrapeSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

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
