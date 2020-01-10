package iths.glenn.drick.model;

import iths.glenn.drick.trip.WayOfTravel;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class TripModel {

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
}
