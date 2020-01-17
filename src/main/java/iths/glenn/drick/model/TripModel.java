package iths.glenn.drick.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Duration minTravellingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Duration maxTravellingTime;
    double totalDistanceInKM;
    double distanceByCarInKM;
    double minTripCharges;
    double maxTripCharges;
    int minCapacityInKilos;
    int maxCapacityInKilos;
}
