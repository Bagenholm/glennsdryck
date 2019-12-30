package iths.glenn.drick.trip;

public class TripDistance {

    String startPoint;
    String endPoint;
    WayOfTravel wayOfTravel;
    boolean includingCar;
    boolean includingTrailer;
    double distanceInKM;
    double charges;
    double travellingTime;
    double capacityInLitres;   //TODO: kapasitet i vikt?

    public TripDistance(String startPoint, String endPoint, WayOfTravel wayOfTravel, boolean includingCar, boolean includingTrailer, double distanceInKM, double charges, double travellingTime, double capacityInLitres) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.wayOfTravel = wayOfTravel;
        this.includingCar = includingCar;
        this.includingTrailer = includingTrailer;
        this.distanceInKM = distanceInKM;
        this.charges = charges;
        this.travellingTime = travellingTime;
        this.capacityInLitres = capacityInLitres;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public WayOfTravel getWayOfTravel() {
        return wayOfTravel;
    }

    public void setWayOfTravel(WayOfTravel wayOfTravel) {
        this.wayOfTravel = wayOfTravel;
    }

    public boolean isIncludingCar() {
        return includingCar;
    }

    public void setIncludingCar(boolean includingCar) {
        this.includingCar = includingCar;
    }

    public boolean isIncludingTrailer() {
        return includingTrailer;
    }

    public void setIncludingTrailer(boolean includingTrailer) {
        this.includingTrailer = includingTrailer;
    }

    public double getDistanceInKM() {
        return distanceInKM;
    }

    public void setDistanceInKM(double distanceInKM) {
        this.distanceInKM = distanceInKM;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public double getTravellingTime() {
        return travellingTime;
    }

    public void setTravellingTime(double travellingTime) {
        this.travellingTime = travellingTime;
    }

    public double getCapacityInLitres() {
        return capacityInLitres;
    }

    public void setCapacityInLitres(double capacityInLitres) {
        this.capacityInLitres = capacityInLitres;
    }
}
