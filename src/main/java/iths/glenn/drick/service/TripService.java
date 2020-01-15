package iths.glenn.drick.service;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.model.TripModel;
import iths.glenn.drick.trip.TripId;
import iths.glenn.drick.trip.UpdateTripRequest;

import java.util.List;

public interface TripService {

    List<TripEntity> listAllTrips();
    List<TripModel> listAllTripsToDestination(String destination);
    TripModel getTripById(TripId tripId);
    TripModel addTrip(TripEntity tripEntity);
    void removeTrip(TripId tripId);
    TripModel updateTrip(TripId tripId, TripEntity tripEntity) throws Exception;
    TripModel updateTripPartially(TripId tripId, UpdateTripRequest updateTripRequest);
}
