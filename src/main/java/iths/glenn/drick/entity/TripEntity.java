package iths.glenn.drick.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import iths.glenn.drick.trip.TripId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;

@Entity
@Table(name = "trips")
@Data
@Getter
@Setter
public class TripEntity implements Serializable {

    @EmbeddedId
    private TripId tripId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Duration minTravellingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Duration maxTravellingTime;
    private double totalDistanceInKM;
    @NotNull(message = "distanceByCarInKM of the trip must be included in request body")
    private double distanceByCarInKM;
    @NotNull(message = "minTripCharges of the trip must be included in request body")
    private double minTripCharges;
    private double maxTripCharges;
    private int minCapacityInKilos;
    private int maxCapacityInKilos;

    public TripEntity() {
        //this.id = startPoint + "-" + endPoint + "-" + tripInfo + "-" + wayOfTravel;  //TODO: Ta bort?
    }

    public TripEntity(TripId tripId, Duration minTravellingTime, Duration maxTravellingTime, double totalDistanceInKM, double distanceByCarInKM, double minTripCharges, double maxTripCharges, int minCapacityInKilos, int maxCapacityInKilos) {
        this.tripId = tripId;
        this.minTravellingTime = minTravellingTime;
        this.maxTravellingTime = maxTravellingTime;
        this.totalDistanceInKM = totalDistanceInKM;
        this.distanceByCarInKM = distanceByCarInKM;
        this.minTripCharges = minTripCharges;
        this.maxTripCharges = maxTripCharges;
        this.minCapacityInKilos = minCapacityInKilos;
        this.maxCapacityInKilos = maxCapacityInKilos;
        //TODO: Ta bort?
        //this.id = startPoint + "-" + endPoint + "-" + tripInfo + "-" + wayOfTravel;  //TODO: Enums för resväg?
        //TODO: använda en egen string för key??  typ göteborg-helsingör-puttgarden
        //TODO: använda nån förkortning som id så de inte blir så långt
        // ...eller bara ta emot en förkortning eller siffra eller nåt som alternativ av användaren som sedan hanteras
        // ...av den servicen för att hämta ut med de långa id:t
    }

}
