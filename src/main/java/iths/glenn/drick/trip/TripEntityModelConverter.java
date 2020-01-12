package iths.glenn.drick.trip;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.TripModel;

import java.util.ArrayList;
import java.util.List;

public class TripEntityModelConverter {

    public TripModel tripEntityToModel(TripEntity tripEntity) {

        TripModel tripModel = new TripModel();

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

        return tripModel;
    }

    public List<TripModel> tripListToModel(List<TripEntity> tripEntityList) {

        List<TripModel> tripModelList = new ArrayList<>();

        tripEntityList.forEach(tripEntity -> tripModelList.add(tripEntityToModel(tripEntity)));
        return tripModelList;
    }
}
