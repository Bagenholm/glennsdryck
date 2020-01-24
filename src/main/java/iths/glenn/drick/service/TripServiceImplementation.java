package iths.glenn.drick.service;

import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.exception.*;
import iths.glenn.drick.model.TripModel;
import iths.glenn.drick.repository.StoreStorage;
import iths.glenn.drick.repository.TripStorage;
import iths.glenn.drick.entity.TripId;
import iths.glenn.drick.trip.UpdateTripRequest;
import iths.glenn.drick.trip.WayOfTravel;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImplementation implements TripService {

    TripStorage tripStorage;
    StoreStorage storeStorage;

    TripEntity deletedTrip;

    //TODO: Detta nånstans?
    /*HashMap<String, Integer> capacityInLitresDrinkType = new HashMap<String, Integer>() {{
        put("liquor", 10);
        put("wine", 90);
        put("fortified wine", 20);
        put("beer", 110);
    }};*/


    public TripServiceImplementation(TripStorage tripStorage, StoreStorage storeStorage) {
        this.tripStorage = tripStorage;
        this.storeStorage = storeStorage;
    }

    public List<TripEntity> getAllTripEntities() {

        List<TripEntity> tripEntityList = tripStorage.findAll();

        if(tripEntityList.isEmpty()) {
            tripEntityList = fillTripList();
        }

        return tripEntityList;
    }

    @Override
    public List<TripModel> listAllTrips() {

        List<TripEntity> tripEntityList = getAllTripEntities();
        return EntityModelConverter.tripListToModel(tripEntityList);
    }

    @Override
    public List<TripModel> listAllTripsToDestination(String destination) {

        List<TripEntity> tripEntityList = getAllTripEntities();

        List<TripEntity> tripsToDestination = tripEntityList.stream()
                .filter(tripEntity -> tripEntity.getTripId().getEndPoint().equals(destination))
                .collect(Collectors.toList());

        if(tripsToDestination.isEmpty()) {
            throw new DestinationDontExistException(String.format("Destination: %s do not exist", destination));
        }

        return EntityModelConverter.tripListToModel(tripsToDestination);
    }

    @Override
    public TripModel getTripById(Map<String, String> tripIdInput) {

        TripId tripId = convertMapToTripId(tripIdInput);
        List<TripEntity> tripEntityList = getAllTripEntities();

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getTripId().equals(tripId)) {
                return EntityModelConverter.tripEntityToModel(tripEntity);
            }
        }

        throw new TripDontExistException(String.format("Trip with id: %s do not exist", tripId));
    }

    @Override
    public TripModel addTrip(TripEntity tripEntity) {

        List<TripEntity> tripEntityList = tripStorage.findAll();

        for (TripEntity trip : tripEntityList) {

            if(trip.getTripId().equals(tripEntity.getTripId())) {
                throw new TripAlreadyExistException(String.format("Trip with id %s already exists! " +
                        "\nCombination of startpoint, endpoint, tripinfo and wayoftravel has to be unique", tripEntity.getTripId()));
            }
        }

        TripEntity tripResponse = tripStorage.save(joinTripWithStoresInEndPointCity(tripEntity));
        return EntityModelConverter.tripEntityToModel(tripResponse);
    }

    @Override
    public void removeTrip(Map<String, String> tripIdInput) {

        TripId tripId = convertMapToTripId(tripIdInput);
        List<TripEntity> tripEntityList = getAllTripEntities();

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getTripId().equals(tripId)) {

                deletedTrip = tripEntity;
                tripStorage.delete(tripEntity);
                return;
            }
        }

        throw new TripDontExistException(String.format("Trip with destination: %s do not exist", tripId));
    }

    @Override
    public TripModel updateTrip(Map<String, String> tripIdInput, TripEntity tripEntity) throws Exception {

        removeTrip(tripIdInput);

        try {
            return addTrip(tripEntity);

        }catch(Exception e) {

            addTrip(joinTripWithStoresInEndPointCity(deletedTrip));
            throw new Exception("Failed to add updated trip " + e.getMessage());
        }
    }

    @Override
    public TripModel updateTripPartially(Map<String, String> tripIdInput, UpdateTripRequest updateTripRequest) {

        TripId tripId = convertMapToTripId(tripIdInput);
        List<TripEntity> tripEntityList = getAllTripEntities();
        TripEntity tripToUpdate;
        TripEntity updatedTrip;

        for (TripEntity tripEntity : tripEntityList) {

            if(tripEntity.getTripId().equals(tripId)) {

                tripToUpdate = updateTripWithRequest(tripEntity, updateTripRequest);
                updatedTrip = tripStorage.save(tripToUpdate);
                return EntityModelConverter.tripEntityToModel(updatedTrip);
            }
        }

        throw new TripDontExistException(String.format("Trip with id: %s do not exist", tripId));
    }


    private TripEntity joinTripWithStoresInEndPointCity(TripEntity tripEntity) {

        List<StoreEntity> storeEntityList = storeStorage.findAll();

        for (StoreEntity storeEntity : storeEntityList) {

            if(storeEntity.getCity().equals(tripEntity.getCity())) {   //TODO: getTripId().getEndPoint()
                storeEntity.addTrip(tripEntity);
            }
        }

        return tripEntity;
    }

    private List<TripEntity> joinTripWithStoresInEndPointCity(List<TripEntity> tripEntityList) {

        for (TripEntity tripEntity : tripEntityList) {

            joinTripWithStoresInEndPointCity(tripEntity);
        }

        return tripEntityList;
    }



    private TripEntity updateTripWithRequest(TripEntity tripEntity, UpdateTripRequest updateTripRequest) {

        Optional.ofNullable(updateTripRequest).ifPresent(updateRequest -> {
            if(updateRequest.getTripId().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update id through patch request!");
            if (updateRequest.getStartPoint().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update startpoint through patch request!");
            if(updateRequest.getEndPoint().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update endpoint through patch request!");
            if(updateRequest.getTripInfo().isPresent())
                throw new NotAllowedToPatchKeyValuesException("Not allowed to update trip-info through patch request!");
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

    private TripId convertMapToTripId(Map<String, String> tripIdInput) {

        String startPoint;
        String endPoint;
        String tripInfo;
        WayOfTravel wayOfTravel;

        if(tripIdInput.containsKey("startPoint")) {
            startPoint = tripIdInput.get("startPoint");
        }
        else {
            throw new IncompleteTripIdException("startPoint Missing in tripId");
        }
        if(tripIdInput.containsKey("endPoint")) {
            endPoint = tripIdInput.get("endPoint");
        }
        else {
            throw new IncompleteTripIdException("endPoint Missing in tripId");
        }
        if(tripIdInput.containsKey("tripInfo")) {
            tripInfo = tripIdInput.get("tripInfo");
        }
        else {
            throw new IncompleteTripIdException("tripInfo Missing in tripId");
        }
        if(tripIdInput.containsKey("wayOfTravel")) {
            if(EnumUtils.isValidEnum(WayOfTravel.class, tripIdInput.get("wayOfTravel").toUpperCase())) {
                wayOfTravel = WayOfTravel.valueOf(tripIdInput.get("wayOfTravel").toUpperCase());
            }
            else {
                throw new IncompleteTripIdException("tripId with that wayOfTravel do not exist!");
            }
        }
        else {
            throw new IncompleteTripIdException("wayOfTravel Missing in tripId");
        }

        return new TripId(startPoint, endPoint, tripInfo, wayOfTravel);
    }

    private List<TripEntity> fillTripList() {

        List<TripEntity> tempTripsList = new ArrayList<>();
        Duration minTripTime;
        Duration maxTripTime;

        minTripTime = Duration.parse("PT3H15M");
        maxTripTime = Duration.parse("PT4H15M");
        TripEntity fredrikshamn = new TripEntity(new TripId("göteborg", "fredrikshamn", "stenaline", WayOfTravel.BY_FOOT), minTripTime, maxTripTime, 104, 0, 50, 50, 0, 0);
        tempTripsList.add(fredrikshamn);
        TripEntity fredrikshamnCar = new TripEntity(new TripId("göteborg", "fredrikshamn", "stenaline", WayOfTravel.CAR), minTripTime, maxTripTime, 104, 0, 380, 380, 450, 450);
        tempTripsList.add(fredrikshamnCar);
        TripEntity fredrikshamnCarTrailer = new TripEntity(new TripId("göteborg", "fredrikshamn", "stenaline", WayOfTravel.CAR_WITH_TRAILER), minTripTime, maxTripTime, 104, 0, 555, 555, 800, 1140);
        tempTripsList.add(fredrikshamnCarTrailer);


        minTripTime = Duration.parse("PT3H22M");
        maxTripTime = Duration.parse("PT3H22M");
        TripEntity copenhagenCar = new TripEntity(new TripId("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.CAR), minTripTime, maxTripTime, 322, 322, 473, 473, 450, 450);
        tempTripsList.add(copenhagenCar);
        minTripTime = Duration.parse("PT4H0M");
        maxTripTime = Duration.parse("PT4H0M");
        TripEntity copenhagenCarTrailer = new TripEntity(new TripId("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.CAR_WITH_TRAILER), minTripTime, maxTripTime, 322, 322, 718, 718, 800, 1140);
        tempTripsList.add(copenhagenCarTrailer);
        minTripTime = Duration.parse("PT4H15M");
        maxTripTime = Duration.parse("PT4H40M");
        TripEntity copenhagenBus = new TripEntity(new TripId("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.BUS), minTripTime, maxTripTime, 322, 0, 130, 400, 20, 20);
        tempTripsList.add(copenhagenBus);
        minTripTime = Duration.parse("PT3H25M");
        maxTripTime = Duration.parse("PT4H30M");
        TripEntity copenhagenTrain = new TripEntity(new TripId("göteborg", "köpenhamn", "öresundsbron", WayOfTravel.TRAIN), minTripTime, maxTripTime, 322, 0, 195, 480, 20, 20);
        tempTripsList.add(copenhagenTrain);


        minTripTime = Duration.parse("PT15H30M");
        maxTripTime = Duration.parse("PT15H30M");
        TripEntity kiel = new TripEntity(new TripId("göteborg", "kiel", "stenaline", WayOfTravel.BY_FOOT), minTripTime, maxTripTime, 0, 0, 400, 900, 0, 0);
        tempTripsList.add(kiel);
        TripEntity kielCar = new TripEntity(new TripId("göteborg", "kiel", "stenaline", WayOfTravel.CAR), minTripTime, maxTripTime, 0, 0, 600, 2750, 450, 450);
        tempTripsList.add(kielCar);
        TripEntity kielCarTrailer = new TripEntity(new TripId("göteborg", "kiel", "stenaline", WayOfTravel.CAR_WITH_TRAILER), minTripTime, maxTripTime, 0, 0, 600, 3040, 800, 1140);
        tempTripsList.add(kielCarTrailer);


        minTripTime = Duration.parse("PT6H14M");
        maxTripTime = Duration.parse("PT6H14M");
        TripEntity puttgardenHelsingorCar = new TripEntity(new TripId("göteborg", "puttgarden", "helsingör", WayOfTravel.CAR), minTripTime, maxTripTime, 455, 431, 723, 852, 450, 450);
        tempTripsList.add(puttgardenHelsingorCar);
        minTripTime = Duration.parse("PT7H14M");
        maxTripTime = Duration.parse("PT7H14M");
        TripEntity puttgardenHelsingorCarTrailer = new TripEntity(new TripId("göteborg", "puttgarden", "helsingör", WayOfTravel.CAR_WITH_TRAILER), minTripTime, maxTripTime, 455, 431, 1073, 1266, 800, 1140);
        tempTripsList.add(puttgardenHelsingorCarTrailer);
        minTripTime = Duration.parse("PT9H0M");
        maxTripTime = Duration.parse("PT10H30M");
        TripEntity puttgardenHelsingorBus = new TripEntity(new TripId("göteborg", "puttgarden", "helsingör", WayOfTravel.BUS), minTripTime, maxTripTime, 455, 0, 375, 375, 100, 100);
        tempTripsList.add(puttgardenHelsingorBus);


        minTripTime = Duration.parse("PT5H47M");
        maxTripTime = Duration.parse("PT5H47M");
        TripEntity puttgardenMalmoCar = new TripEntity(new TripId("göteborg", "puttgarden", "malmö", WayOfTravel.CAR), minTripTime, maxTripTime, 494, 475, 808, 1568, 450, 450);
        tempTripsList.add(puttgardenMalmoCar);
        minTripTime = Duration.parse("PT6H52M");
        maxTripTime = Duration.parse("PT6H52M");
        TripEntity puttgardenMalmoCarTrailer = new TripEntity(new TripId("göteborg", "puttgarden", "malmö", WayOfTravel.CAR_WITH_TRAILER), minTripTime, maxTripTime, 494, 475, 1318, 2078, 800, 1140);
        tempTripsList.add(puttgardenMalmoCarTrailer);
        minTripTime = Duration.parse("PT8H25M");
        maxTripTime = Duration.parse("PT11H10M");
        TripEntity puttgardenMalmoBus = new TripEntity(new TripId("göteborg", "puttgarden", "malmö", WayOfTravel.BUS), minTripTime, maxTripTime, 494, 0, 260, 750, 20, 20);
        tempTripsList.add(puttgardenMalmoBus);

        minTripTime = Duration.parse("PT0H01M");
        maxTripTime = Duration.parse("PT1H00M");
        TripEntity systembolagetByFoot = new TripEntity(new TripId("göteborg", "göteborg", "", WayOfTravel.BY_FOOT), minTripTime, maxTripTime, 0, 0, 0, 0, 0, 0);
        tempTripsList.add(systembolagetByFoot);

        return tripStorage.saveAll(tempTripsList);
                //joinTripWithStoresInEndPointCity(tempTripsList));
    }
}
