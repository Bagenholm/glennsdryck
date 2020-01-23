package iths.glenn.drick.service;

import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.StoreModel;
import iths.glenn.drick.model.TripModel;

import java.util.ArrayList;
import java.util.List;

public class EntityModelConverter {

    public static TripModel tripEntityToModel(TripEntity tripEntity) {

        TripModel tripModel = new TripModel();
        List<StoreModel> stores = new ArrayList<>();

        tripModel.setStartPoint(tripEntity.getTripId().getStartPoint());
        tripModel.setEndPoint(tripEntity.getTripId().getEndPoint());
        tripModel.setTripInfo(tripEntity.getTripId().getTripInfo());
        tripModel.setWayOfTravel(tripEntity.getTripId().getWayOfTravel());
        tripModel.setMinTravellingTime(tripEntity.getMinTravellingTime());
        tripModel.setMaxTravellingTime(tripEntity.getMaxTravellingTime());
        tripModel.setTotalDistanceInKM(tripEntity.getTotalDistanceInKM());
        tripModel.setDistanceByCarInKM(tripEntity.getDistanceByCarInKM());
        tripModel.setMinTripCharges(tripEntity.getMinTripCharges());
        tripModel.setMaxTripCharges(tripEntity.getMaxTripCharges());
        tripModel.setMinCapacityInKilos(tripEntity.getMinCapacityInKilos());
        tripModel.setMaxCapacityInKilos(tripEntity.getMaxCapacityInKilos());

        tripEntity.getStores().forEach(storeEntity -> stores.add(storeEntityToModel(storeEntity)));
        tripModel.setStores(stores);

        return tripModel;
    }

    public static List<TripModel> tripListToModel(List<TripEntity> tripEntityList) {

        List<TripModel> tripModelList = new ArrayList<>();

        tripEntityList.forEach(tripEntity -> tripModelList.add(tripEntityToModel(tripEntity)));
        return tripModelList;
    }

    public static StoreModel storeEntityToModel(StoreEntity storeEntity) {

        StoreModel storeModel = new StoreModel();
        List<TripModel> trips = new ArrayList<>();

        storeModel.setStoreName(storeEntity.getStoreName());
        storeModel.setCurrency(storeEntity.getCurrency());
        storeModel.setCity(storeEntity.getCity());

        storeEntity.getTrips().forEach(tripEntity -> trips.add(tripEntityToModel(tripEntity)));
        storeModel.setTrips(trips);
        return storeModel;
    }

    public static List<StoreModel> storeListToModel(List<StoreEntity> storeEntityList) {

        List<StoreModel> storeModelList = new ArrayList<>();

        storeEntityList.forEach(storeEntity -> storeModelList.add(storeEntityToModel(storeEntity)));
        return storeModelList;
    }
}
