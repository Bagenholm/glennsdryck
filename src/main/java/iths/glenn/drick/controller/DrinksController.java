package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.service.DrinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/drinks")
public class DrinksController {

    @Autowired
    DrinksService drinksService;

    @Autowired
    DrinkStorage drinkStorage;

    @GetMapping("")
    public List<DrinkEntity> getAll() {
        return drinksService.findAll();
    }

    @GetMapping("/volume/{volume}")
    public List<DrinkEntity> getByVolume(@PathVariable float volume) {
        return drinksService.findAllByVolume(volume);
    }

    @GetMapping("/app/")
    public List<DrinkEntity> getAllByApkDesc() {
        return drinksService.findAllByApkDesc();
    }

    @GetMapping("/app/store/{store}/{limit}")
    public List<DrinkEntity> findAmountBestApkFromStore(@PathVariable String store, @PathVariable int limit) {
        return drinksService.findAmountBestApkFromStore(store, limit);
    }

    @GetMapping("/app/store/all/{limit}")
    public List<DrinkEntity> findTenBestApkFromAllStores(@PathVariable int limit) {
        return drinksService.findAmountBestApkFromEachStore(limit);
    }

    @GetMapping("/app/store/all/{type}/{limit}")
    public List<DrinkEntity> findTenBestApkFromAllStoresByType(@PathVariable String type, @PathVariable int limit) {
        return drinksService.findAmountBestApkFromEachStoreByType(type, limit);
    }

    @GetMapping("/app/type/{type}/{limit}")
    public List<DrinkEntity> findTenBestApkByType(@PathVariable String type, @PathVariable int limit) {
        return drinksService.findAmountBestApkByType(type, limit);
    }

    @GetMapping("/name/{name}")
    public List<DrinkEntity> findByName(@PathVariable String name) {
        return drinksService.findByName(name);
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