package iths.glenn.drick.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.ws.rs.Path;
import java.util.*;

@RestController
@RequestMapping("/drinks")
public class DrinksController {

    @Autowired
    DrinkStorage drinkStorage;

    @GetMapping("")
    public List<DrinkEntity> getAll() {
        return drinkStorage.findAll();
    }

    @GetMapping("/bestAlcoholPrice/{amount}")
    public List<DrinkEntity> getBestAlcoholPrice(@PathVariable int amount) {
        return Collections.emptyList();
    }

    @GetMapping("/volume/{volume}")
    public List<DrinkEntity> getByVolume(@PathVariable float volume) {
        return drinkStorage.findByVolume(volume);
    }

    @GetMapping("/app/")
    public List<DrinkEntity> getAllByAppAsc() {
        return drinkStorage.findAllDrinks(JpaSort.unsafe("alcoholPerPrice").descending());
    }

    @GetMapping("/app/store/{store}/{limit}")
    List<DrinkEntity> findTenBestApkFromStore(@PathVariable String store, @PathVariable int limit) {
        return drinkStorage.findAllByStoreEquals(store, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    @GetMapping("/app/store/all/{limit}")
    List<DrinkEntity> findTenBestApkFromAllStores(@PathVariable int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreEquals("systembolaget", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("calle", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("stenaline", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("fleggaard", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("driveinbottleshop", limitAndsort));

        return drinks;
    }

    @GetMapping("/app/store/all/{type}/{limit}")
    List<DrinkEntity> findTenBestApkFromAllStoresByType(@PathVariable String type, @PathVariable int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("systembolaget", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("calle", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("stenaline", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("fleggaard", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("driveinbottleshop", type, limitAndsort));

        return drinks;
    }

    @GetMapping("/app/type/{type}/{limit}")
    List<DrinkEntity> findTenBestApkByType(@PathVariable String type, @PathVariable int limit) {
        return drinkStorage.findAllByTypeEquals(type, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    /* @GetMapping("/exchange/{currency}")
    public float getCurrencyExchange(@PathVariable String currency) {
        WebClient webClient = WebClient.create("https://api.exchangeratesapi.io/latest");

        Mono<JsonNode> testMono = webClient.get()
                .uri("?base=SEK&symbols=" + currency)
                .retrieve()
                .bodyToMono(JsonNode.class);

        String result = testMono.block().get("rates").get(currency).asText();
        return 1 / Float.parseFloat(result);
    } */
}