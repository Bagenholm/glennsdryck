package iths.glenn.drick;

import iths.glenn.drick.scrapequeue.ScrapeReceiver;
import iths.glenn.drick.scrapequeue.ScrapeSender;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DrickApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrickApplication.class, args);
    }

    @Bean
    public Queue queue() {
        return new Queue("scrape", false);
    }

}
