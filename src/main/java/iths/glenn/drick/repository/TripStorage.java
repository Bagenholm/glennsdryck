package iths.glenn.drick.repository;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.TripModel;

import java.util.List;

public interface TripStorage {

    List<TripEntity> getAllTrips();
    List<TripModel> getAllTripsToDestination(String destination);  //TODO: Inte här
    TripEntity getTrip(String id);  //TODO: Inte här
    TripEntity addTrip(TripEntity tripEntity);
    TripEntity updateTrip(TripEntity tripEntity);
    void deleteTrip(TripEntity tripEntity);
}
