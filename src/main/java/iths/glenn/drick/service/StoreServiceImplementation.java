package iths.glenn.drick.service;

import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.exception.StoreDontExistException;
import iths.glenn.drick.model.StoreModel;
import iths.glenn.drick.repository.StoreStorage;
import iths.glenn.drick.repository.TripStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImplementation implements StoreService {

    StoreStorage storeStorage;
    TripStorage tripStorage;

    public StoreServiceImplementation(StoreStorage storeStorage, TripStorage tripStorage) {
        this.storeStorage = storeStorage;
        this.tripStorage = tripStorage;
    }

    public List<StoreEntity> getAllStoreEntities() {

        List<StoreEntity> storeEntityList = storeStorage.findAll();

        return storeEntityList;
    }

    @Override
    public List<StoreModel> listAllStores() {

        List<StoreEntity> storeEntityList = getAllStoreEntities();
        return EntityModelConverter.storeListToModel(storeEntityList);
    }

    @Override
    public List<StoreModel> listStoresInCity(String city) {

        List<StoreEntity> storeEntityList = getAllStoreEntities();

        List<StoreEntity> storesInCity = storeEntityList.stream()
                .filter(storeEntity -> storeEntity.getCity().equals(city))
                .collect(Collectors.toList());

        if(storesInCity.isEmpty()) {
            throw new StoreDontExistException(String.format("There is no stores in %s", city));
        }

        return EntityModelConverter.storeListToModel(storesInCity);
    }

    @Override
    public StoreModel getStoreByName(String storeName) {

        List<StoreEntity> storeEntityList = getAllStoreEntities();

        for (StoreEntity storeEntity : storeEntityList) {

            if(storeEntity.getStoreName().equals(storeName)) {
                return EntityModelConverter.storeEntityToModel(storeEntity);
            }
        }

        throw new StoreDontExistException(String.format("Store with %s do not exist", storeName));
    }

    @Override
    public List<StoreModel> joinStoresWithTrips() {

        List<StoreEntity> storeEntityList = getAllStoreEntities();
        List<StoreEntity> joinedStores = joinStoreWithTripsToCity(storeEntityList);

        return EntityModelConverter.storeListToModel(joinedStores);
    }


    private StoreEntity joinStoreWithTripsToCity(StoreEntity storeEntity) {

        List<TripEntity> tripEntityList = tripStorage.findAll();

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getCity().equals(storeEntity.getCity())) {
                storeEntity.addTrip(tripEntity);
                storeStorage.save(storeEntity);
            }
        }

        return storeEntity;
    }

    //TODO: Anropa efter scrape görs?
    private List<StoreEntity> joinStoreWithTripsToCity(List<StoreEntity> stores) {

        List<StoreEntity> storeEntityList = new ArrayList<>();

        for (StoreEntity storeEntity : stores) {
            storeEntityList.add(joinStoreWithTripsToCity(storeEntity));
        }

        return storeEntityList;
    }
}
