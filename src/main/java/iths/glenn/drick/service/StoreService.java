package iths.glenn.drick.service;

import iths.glenn.drick.model.StoreModel;

import java.util.List;

public interface StoreService {

    List<StoreModel> listAllStores();
    List<StoreModel> listStoresInCity(String city);
    StoreModel getStoreByName(String name);
    List<StoreModel> joinStoresWithTrips();
}
