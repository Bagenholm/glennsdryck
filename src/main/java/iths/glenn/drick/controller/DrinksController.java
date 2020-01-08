package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinksController {

    DrinkStorage drinkStorage;

    public DrinksController(DrinkStorage drinkStorage) {
        this.drinkStorage = drinkStorage;
    }

    @GetMapping("")
    public List<DrinkEntity> getAll() {
        return drinkStorage.findAll();
    }

    @GetMapping("/exchange/{currency}")
    public Flux<String> getCurrencyExchange(@PathVariable String currency) {
        WebClient webClient = WebClient.create("https://api.exchangeratesapi.io/latest");
        Flux<String> testFlux = webClient.get().uri("?base=SEK&symbols=" + currency)
                .retrieve().bodyToFlux(String.class);

        return testFlux;
    }
}
