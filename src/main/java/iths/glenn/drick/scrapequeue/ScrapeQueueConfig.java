package iths.glenn.drick.scrapequeue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"scrapequeue", "scrape"})
@Configuration
public class ScrapeQueueConfig {

    @Bean
    public ScrapeReceiver receiver() {
        return new ScrapeReceiver();
    }

    @Bean
    public ScrapeSender sender() {
        return new ScrapeSender();
    }
}