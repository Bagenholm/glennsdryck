package iths.glenn.drick.trip;

import java.time.Duration;
import java.util.Optional;

public class UpdateTripRequest {

    String id;
    String startPoint;
    String endPoint;
    String tripInfo;
    WayOfTravel wayOfTravel;
    Duration minTravellingTime;
    Duration maxTravellingTime;
    double totalDistanceInKM;
    double distanceByCarInKM;
    double minTripCharges;
    double maxTripCharges;
    int minCapacityInKilos;
    int maxCapacityInKilos;

    public UpdateTripRequest() {
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<String> getStartPoint() {
        return Optional.ofNullable(startPoint);
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public Optional<String> getEndPoint() {
        return Optional.ofNullable(endPoint);
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Optional<String> getTripInfo() {
        return Optional.ofNullable(tripInfo);
    }

    public void setTripInfo(String tripInfo) {
        this.tripInfo = tripInfo;
    }

    public Optional<WayOfTravel> getWayOfTravel() {
        return Optional.ofNullable(wayOfTravel);
    }

    public void setWayOfTravel(WayOfTravel wayOfTravel) {
        this.wayOfTravel = wayOfTravel;
    }

    public Optional<Duration> getMinTravellingTime() {
        return Optional.ofNullable(minTravellingTime);
    }

    public void setMinTravellingTime(Duration minTravellingTime) {
        this.minTravellingTime = minTravellingTime;
    }

    public Optional<Duration> getMaxTravellingTime() {
        return Optional.ofNullable(maxTravellingTime);
    }

    public void setMaxTravellingTime(Duration maxTravellingTime) {
        this.maxTravellingTime = maxTravellingTime;
    }

    public Optional<Double> getTotalDistanceInKM() {
        return Optional.of(totalDistanceInKM);
    }

    public void setTotalDistanceInKM(double totalDistanceInKM) {
        this.totalDistanceInKM = totalDistanceInKM;
    }

    public Optional<Double> getDistanceByCarInKM() {
        return Optional.of(distanceByCarInKM);
    }

    public void setDistanceByCarInKM(double distanceByCarInKM) {
        this.distanceByCarInKM = distanceByCarInKM;
    }

    public Optional<Double> getMinTripCharges() {
        return Optional.of(minTripCharges);
    }

    public void setMinTripCharges(double minTripCharges) {
        this.minTripCharges = minTripCharges;
    }

    public Optional<Double> getMaxTripCharges() {
        return Optional.of(maxTripCharges);
    }

    public void setMaxTripCharges(double maxTripCharges) {
        this.maxTripCharges = maxTripCharges;
    }

    public Optional<Integer> getMinCapacityInKilos() {
        return Optional.of(minCapacityInKilos);
    }

    public void setMinCapacityInKilos(int minCapacityInKilos) {
        this.minCapacityInKilos = minCapacityInKilos;
    }

    public Optional<Integer> getMaxCapacityInKilos() {
        return Optional.of(maxCapacityInKilos);
    }

    public void setMaxCapacityInKilos(int maxCapacityInKilos) {
        this.maxCapacityInKilos = maxCapacityInKilos;
    }
}
