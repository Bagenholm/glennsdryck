package iths.glenn.drick.service;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.TripModel;
import iths.glenn.drick.trip.UpdateTripRequest;

import java.util.List;
import java.util.Map;

public interface TripService {

    List<TripEntity> listAllTrips();
    List<TripModel> listAllTripsToDestination(String destination);
    TripModel getTripById(Map<String, String> tripId);
    TripModel addTrip(TripEntity tripEntity);
    void removeTrip(Map<String, String> tripId);
    TripModel updateTrip(Map<String, String> tripId, TripEntity tripEntity) throws Exception;
    TripModel updateTripPartially(Map<String, String> tripId, UpdateTripRequest updateTripRequest);
}
