package iths.glenn.drick.trip;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.exception.DestinationDontExistException;
import iths.glenn.drick.exception.NotAllowedToPatchKeyValuesException;
import iths.glenn.drick.exception.TripAlreadyExistException;
import iths.glenn.drick.exception.TripDontExistException;
import iths.glenn.drick.model.TripModel;
import iths.glenn.drick.repository.TripStorage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImplementation implements TripService {

    TripStorage tripStorage;
    TripEntityModelConverter tripEntityModelConverter;

    TripEntity deletedTrip;

    //TODO: Detta nånstans?
    /*HashMap<String, Integer> capacityInLitresDrinkType = new HashMap<String, Integer>() {{
        put("liquor", 10);
        put("wine", 90);
        put("fortified wine", 20);
        put("beer", 110);
    }};

    double fuelConsumptionLitrePerMile;*/


    public TripServiceImplementation(TripStorage tripStorage) {
        this.tripStorage = tripStorage;
    }

    @Override
    public List<TripEntity> listAllTrips() {

        List<TripEntity> tripEntityList = tripStorage.findAll();

        if(tripEntityList.isEmpty()) {
            fillTripList();
        }

        return tripEntityList;
    }

    @Override
    public List<TripModel> listAllTripsToDestination(String destination) {

        List<TripEntity> tripEntityList = listAllTrips();

        List<TripEntity> tripsToDestination = tripEntityList.stream()
                .filter(tripEntity -> tripEntity.getEndPoint().equals(destination))
                .collect(Collectors.toList());

        if(tripsToDestination.isEmpty()) {
            throw new DestinationDontExistException(String.format("Destination: %s do not exist", destination));
        }

        return tripEntityModelConverter.tripListToModel(tripsToDestination);
    }

    @Override
    public TripModel getTripById(String tripId) {

        List<TripEntity> tripEntityList = listAllTrips();

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getId().equals(tripId)) {
                return tripEntityModelConverter.tripEntityToModel(tripEntity);
            }
        }

        throw new TripDontExistException(String.format("Trip with id: %s do not exist", tripId));
    }

    @Override
    public TripModel addTrip(TripEntity tripEntity) {

        List<TripEntity> tripEntityList = listAllTrips();

        for (TripEntity trip : tripEntityList) {

            if(trip.getId().equals(tripEntity.getId())) {
                throw new TripAlreadyExistException(String.format("Trip with id %s already exist", tripEntity.getId()));
            }
        }

        TripEntity tripResponse = tripStorage.save(tripEntity);
        return tripEntityModelConverter.tripEntityToModel(tripResponse);
    }

    @Override
    public void removeTrip(String tripId) {

        List<TripEntity> tripEntityList = listAllTrips();

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getId().equals(tripId)) {

                deletedTrip = tripStorage.getOne(tripId);
                tripStorage.delete(tripEntity);
                return;
            }
        }

        throw new TripDontExistException(String.format("Trip with destination: %s do not exist", tripId));
    }

    @Override
    public TripModel updateTrip(String tripId, TripEntity tripEntity) throws Exception {

        removeTrip(tripId);

        try {
            return addTrip(tripEntity);
        }
        catch (Exception e) {

            addTrip(deletedTrip);
            throw new Exception("Failed to add updated trip " + e.getMessage());
        }
    }

    @Override
    public TripModel updateTripPartially(String tripId, UpdateTripRequest updateTripRequest) {

        List<TripEntity> tripEntityList = listAllTrips();
        TripEntity tripToUpdate;
        TripEntity updatedTrip;

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getId().equals(tripId)) {

                tripToUpdate = updateTripWithRequest(tripEntity, updateTripRequest);
                updatedTrip = tripStorage.save(tripToUpdate);
                return tripEntityModelConverter.tripEntityToModel(updatedTrip);
            }
        }

        throw new TripDontExistException(String.format("Trip with id: %s do not exist", tripId));
    }

    private TripEntity updateTripWithRequest(TripEntity tripEntity, UpdateTripRequest updateTripRequest) {

        Optional.ofNullable(updateTripRequest).ifPresent(updateRequest -> {
            if(updateRequest.getId().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update id through patch request!");
            if (updateRequest.getStartPoint().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update startpoint through patch request!");
            if(updateRequest.getEndPoint().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update endpoint through patch request!");
            if(updateRequest.getTripInfo().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update trip info through patch request!");
            if(updateRequest.getWayOfTravel().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update way of travel through patch request!");

            updateRequest.getMinTravellingTime().filter(value -> !value.isZero()).ifPresent(tripEntity::setMinTravellingTime);
            updateRequest.getMaxTravellingTime().filter(value -> !value.isZero()).ifPresent(tripEntity::setMaxTravellingTime);
            updateRequest.getTotalDistanceInKM().ifPresent(tripEntity::setTotalDistanceInKM);
            updateRequest.getDistanceByCarInKM().ifPresent(tripEntity::setDistanceByCarInKM);
            updateRequest.getMinTripCharges().ifPresent(tripEntity::setMinTripCharges);
            updateRequest.getMaxTripCharges().ifPresent(tripEntity::setMaxTripCharges);
            updateRequest.getMinCapacityInKilos().ifPresent(tripEntity::setMinCapacityInKilos);
            updateRequest.getMaxCapacityInKilos().ifPresent(tripEntity::setMaxCapacityInKilos);
        });

        return tripEntity;
    }

    private void fillTripList() {

        Duration minTripTime;
        Duration maxTripTime;

        minTripTime = Duration.parse("PT3H15M");
        maxTripTime = Duration.parse("PT4H15M");
        TripEntity fredrikshamn = new TripEntity("göteborg", "fredrikshamn", "stenaline", WayOfTravel.BY_FOOT, minTripTime, maxTripTime, 104, 0, 50, 50, 0, 0);
        addTrip(fredrikshamn);
        TripEntity fredrikshamnCar = new TripEntity("göteborg", "fredrikshamn", "stenaline", WayOfTravel.CAR, minTripTime, maxTripTime, 104, 0, 380, 380, 450, 450);
        addTrip(fredrikshamnCar);
        TripEntity fredrikshamnCarTrailer = new TripEntity("göteborg", "fredrikshamn", "stenaline", WayOfTravel.CAR_WITH_TRAILER, minTripTime, maxTripTime, 104, 0, 555, 555, 800, 1140);
        addTrip(fredrikshamnCarTrailer);


        minTripTime = Duration.parse("PT3H22M");
        maxTripTime = Duration.parse("PT3H22M");
        TripEntity copenhagenCar = new TripEntity("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.CAR, minTripTime, maxTripTime, 322, 322, 473, 473, 450, 450);
        addTrip(copenhagenCar);
        minTripTime = Duration.parse("PT4H0M");
        maxTripTime = Duration.parse("PT4H0M");
        TripEntity copenhagenCarTrailer = new TripEntity("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.CAR_WITH_TRAILER, minTripTime, maxTripTime, 322, 322, 718, 718, 800, 1140);
        addTrip(copenhagenCarTrailer);
        minTripTime = Duration.parse("PT4H15M");
        maxTripTime = Duration.parse("PT4H40M");
        TripEntity copenhagenBus = new TripEntity("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.BUS, minTripTime, maxTripTime, 322, 0, 130, 400, 20, 20);
        addTrip(copenhagenBus);
        minTripTime = Duration.parse("PT3H25M");
        maxTripTime = Duration.parse("PT4H30M");
        TripEntity copenhagenTrain = new TripEntity("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.TRAIN, minTripTime, maxTripTime, 322, 0, 195, 480, 20, 20);
        addTrip(copenhagenTrain);


        minTripTime = Duration.parse("PT15H30M");
        maxTripTime = Duration.parse("PT15H30M");
        TripEntity kiel = new TripEntity("göteborg", "kiel", "stenaline", WayOfTravel.BY_FOOT, minTripTime, maxTripTime, 0, 0, 400, 900, 0, 0);
        addTrip(kiel);
        TripEntity kielCar = new TripEntity("göteborg", "kiel", "stenaline", WayOfTravel.CAR, minTripTime, maxTripTime, 0, 0, 600, 2750, 450, 450);
        addTrip(kielCar);
        TripEntity kielCarTrailer = new TripEntity("göteborg", "kiel", "stenaline", WayOfTravel.CAR_WITH_TRAILER, minTripTime, maxTripTime, 0, 0, 600, 3040, 800, 1140);
        addTrip(kielCarTrailer);


        minTripTime = Duration.parse("PT6H14M");
        maxTripTime = Duration.parse("PT6H14M");
        TripEntity puttgardenHelsingorCar = new TripEntity("göteborg", "puttgarden", "helsingör", WayOfTravel.CAR, minTripTime, maxTripTime, 455, 431, 723, 852, 450, 450);
        addTrip(puttgardenHelsingorCar);
        minTripTime = Duration.parse("PT7H14M");
        maxTripTime = Duration.parse("PT7H14M");
        TripEntity puttgardenHelsingorCarTrailer = new TripEntity("göteborg", "puttgarden", "helsingör", WayOfTravel.CAR_WITH_TRAILER, minTripTime, maxTripTime, 455, 431, 1073, 1266, 800, 1140);
        addTrip(puttgardenHelsingorCarTrailer);
        minTripTime = Duration.parse("PT9H0M");
        maxTripTime = Duration.parse("PT10H30M");
        TripEntity puttgardenHelsingorBus = new TripEntity("göteborg", "puttgarden", "helsingör", WayOfTravel.BUS, minTripTime, maxTripTime, 455, 0, 375, 375, 100, 100);
        addTrip(puttgardenHelsingorBus);


        minTripTime = Duration.parse("PT5H47M");
        maxTripTime = Duration.parse("PT5H47M");
        TripEntity puttgardenMalmoCar = new TripEntity("göteborg", "puttgarden", "malmö", WayOfTravel.CAR, minTripTime, maxTripTime, 494, 475, 808, 1568, 450, 450);
        addTrip(puttgardenMalmoCar);
        minTripTime = Duration.parse("PT6H52M");
        maxTripTime = Duration.parse("PT6H52M");
        TripEntity puttgardenMalmoCarTrailer = new TripEntity("göteborg", "puttgarden", "malmö", WayOfTravel.CAR_WITH_TRAILER, minTripTime, maxTripTime, 494, 475, 1318, 2078, 800, 1140);
        addTrip(puttgardenMalmoCarTrailer);
        minTripTime = Duration.parse("PT8H25M");
        maxTripTime = Duration.parse("PT11H10M");
        TripEntity puttgardenMalmoBus = new TripEntity("göteborg", "puttgarden", "malmö", WayOfTravel.BUS, minTripTime, maxTripTime, 494, 0, 260, 750, 20, 20);
        addTrip(puttgardenMalmoBus);
    }
}
