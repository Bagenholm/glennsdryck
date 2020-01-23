package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.service.DrinksService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/apk/")
    public List<DrinkEntity> getAllByApkDesc() {
        return drinksService.findAllByApkDesc();
    }

    @GetMapping("/apk/store/{store}/{limit}")
    public List<DrinkEntity> findAmountBestApkFromStore(@PathVariable String store, @PathVariable int limit) {
        return drinksService.findAmountBestApkFromStore(store, limit);
    }

    @GetMapping("/apk/store/all/{limit}")
    public List<DrinkEntity> findAmountBestApkFromAllStores(@PathVariable int limit) {
        return drinksService.findAmountBestApkFromEachStore(limit);
    }

    @GetMapping("/app/store/all/{type}/{limit}")
    public List<DrinkEntity> findAmountBestApkFromAllStoresByType(@PathVariable String type, @PathVariable int limit) {
        return drinksService.findAmountBestApkFromEachStoreByType(type, limit);
    }

    @GetMapping("/app/type/{type}/{limit}")
    public List<DrinkEntity> findAmountBestApkByType(@PathVariable String type, @PathVariable int limit) {
        return drinksService.findAmountBestApkByType(type, limit);
    }

    @GetMapping("/name/{name}")
    public List<DrinkEntity> findByName(@PathVariable String name) {
        return drinksService.findByName(name);
    }
}