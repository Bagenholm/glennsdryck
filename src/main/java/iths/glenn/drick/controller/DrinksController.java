package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

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

    @GetMapping("/bestAlcoholPrice/{amount}")
    public List<DrinkEntity> getBestAlcoholPrice(@PathVariable int amount) {
        return Collections.emptyList();
    }

    @GetMapping("/exchange/{currency}")
    public String getCurrencyExchange(@PathVariable String currency) {
        WebClient webClient = WebClient.create("https://api.exchangeratesapi.io/latest");

        Mono<String> testMono = webClient.get()
                .uri("?base=SEK&symbols=" + currency)
                .retrieve()
                .bodyToMono(String.class);

        System.err.println(testMono.block());
        return testMono.block();
        //return testFlux;
    }
}
