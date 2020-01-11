package iths.glenn.drick.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CurrencyExchangeRateService {
    public static float exchangeRate(String currency) {
        try {
            WebClient webClient = WebClient.create("https://api.exchangeratesapi.io/latest");

            Mono<JsonNode> testMono = webClient.get()
                    .uri("?base=SEK&symbols=" + currency)
                    .retrieve()
                    .bodyToMono(JsonNode.class);

            String result = testMono.block().get("rates").get(currency).asText();
            return 1 / Float.parseFloat(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1f;
    }
}
