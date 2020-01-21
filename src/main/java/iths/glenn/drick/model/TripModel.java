package iths.glenn.drick.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import iths.glenn.drick.trip.WayOfTravel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    List<StoreModel> stores = new ArrayList<>();
}
