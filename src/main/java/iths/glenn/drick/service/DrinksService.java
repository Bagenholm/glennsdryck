package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrinksService {

    @Autowired
    DrinkStorage drinkStorage;

    public List<DrinkEntity> findAll() {
        return drinkStorage.findAll();
    }

    public List<DrinkEntity> findAllByVolume(float volume) {
        return drinkStorage.findByVolume(volume);
    }

    public List<DrinkEntity> findAllByApkDesc() {
        return drinkStorage.findAllDrinks(JpaSort.unsafe("alcoholPerPrice").descending());
    }

    public List<DrinkEntity> findAmountBestApkFromStore(String store, int limit) {
        return drinkStorage.findAllByStoreEquals(store, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    public List<DrinkEntity> findAmountBestApkFromEachStore(int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreEquals("systembolaget", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("calle", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("stenaline", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("fleggaard", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEquals("driveinbottleshop", limitAndsort));

        return drinks;
    }

    public List<DrinkEntity> findAmountBestApkFromEachStoreByType(String type, int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("systembolaget", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("calle", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("stenaline", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("fleggaard", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreEqualsAndTypeEquals("driveinbottleshop", type, limitAndsort));

        return drinks;
    }

    public List<DrinkEntity> findAmountBestApkByType(String type, int limit) {
        return drinkStorage.findAllByTypeEquals(type, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    public List<DrinkEntity> findByName(String name) {
        return drinkStorage.findAllByNameContaining(name);
    }

}
