package iths.glenn.drick.trip;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.TripModel;

import java.util.List;

public interface TripService {

    List<TripEntity> listAllTrips();
    List<TripModel> listAllTripsToDestination(String destination);
    TripModel getTripById(String tripId);
    TripModel addTrip(TripEntity tripEntity);
    void removeTrip(String tripId);
    TripModel updateTrip(String tripId, TripEntity tripEntity) throws Exception;
    TripModel updateTripPartially(String tripId, UpdateTripRequest updateTripRequest);
}
