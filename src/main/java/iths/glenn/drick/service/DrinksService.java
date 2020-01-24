package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

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
        return drinkStorage.findAllByStoreNameEquals(store, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    public List<DrinkEntity> findAmountBestApkFromEachStore(int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreNameEquals("systembolaget", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEquals("calle", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEquals("stenaline", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEquals("fleggaard", limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEquals("driveinbottleshop", limitAndsort));

        return drinks;
    }

    public List<DrinkEntity> findAmountBestApkFromEachStoreByType(String type, int limit) {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        Pageable limitAndsort = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice"));
        drinks.addAll(drinkStorage.findAllByStoreNameEqualsAndTypeEquals("systembolaget", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEqualsAndTypeEquals("calle", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEqualsAndTypeEquals("stenaline", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEqualsAndTypeEquals("fleggaard", type, limitAndsort));
        drinks.addAll(drinkStorage.findAllByStoreNameEqualsAndTypeEquals("driveinbottleshop", type, limitAndsort));

        return drinks;
    }

    public List<DrinkEntity> findAmountBestApkByType(String type, int limit) {
        return drinkStorage.findAllByTypeEquals(type, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "alcoholPerPrice")));
    }

    public List<DrinkEntity> findByName(String name) {
        return drinkStorage.findAllByNameContaining(name);
    }

}
