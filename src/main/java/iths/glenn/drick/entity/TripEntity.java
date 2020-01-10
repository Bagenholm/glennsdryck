package iths.glenn.drick.entity;

import iths.glenn.drick.trip.WayOfTravel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Entity
@Data
@Getter
@Setter
public class TripEntity {

    @Id
    String id;
    @NotNull
    String startPoint;
    @NotNull
    String endPoint;
    @NotNull
    String tripInfo;
    @NotNull
    WayOfTravel wayOfTravel;
    Duration minTravellingTime;
    Duration maxTravellingTime;
    double totalDistanceInKM;
    @NotNull
    double distanceByCarInKM;
    @NotNull
    double minTripCharges;
    double maxTripCharges;
    int minCapacityInKilos;
    int maxCapacityInKilos;

    public TripEntity() {
    }

    public TripEntity(String startPoint, String endPoint, String tripInfo, WayOfTravel wayOfTravel, Duration minTravellingTime, Duration maxTravellingTime, double totalDistanceInKM, double distanceByCarInKM, double minTripCharges, double maxTripCharges, int minCapacityInKilos, int maxCapacityInKilos) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.tripInfo = tripInfo;
        this.wayOfTravel = wayOfTravel;
        this.minTravellingTime = minTravellingTime;
        this.maxTravellingTime = maxTravellingTime;
        this.totalDistanceInKM = totalDistanceInKM;
        this.distanceByCarInKM = distanceByCarInKM;
        this.minTripCharges = minTripCharges;
        this.maxTripCharges = maxTripCharges;
        this.minCapacityInKilos = minCapacityInKilos;
        this.maxCapacityInKilos = maxCapacityInKilos;
        this.id = startPoint + "-" + endPoint + "-" + tripInfo + "-" + wayOfTravel;  //TODO: Enums för resväg?
        //TODO: använda en egen string för key??  typ göteborg-helsingör-puttgarden
    }

}
