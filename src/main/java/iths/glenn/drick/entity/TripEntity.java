package iths.glenn.drick.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import iths.glenn.drick.trip.TripId;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@Getter
@Setter
public class TripEntity implements Serializable {


    //TODO: Sätta @Column(name = {name}) på alla fält?   t.ex. name = trip_id   eller  min_travelling_time
    //TODO: behövs @MapsId??    för composite keys    eller är detta bara när man behöver en till separat klass? i databasen
    //TODO: ...räknas TripId som en sån extra klass?   den separata klassen ska va typ om relationen mellan 2 entiteter ska ha ett attribut



    @EmbeddedId
    private TripId tripId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
   // @Pattern(message = "Wrong format of duration for minTravellingTime", regexp = "PTnHnM")  //TODO: Ersätta regex mot nåt programmet kan hantera, bokstäver och siffror typ
    private Duration minTravellingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    //@Pattern(message = "Wrong format of duration for maxTravellingTime", regexp = "PTnHnM")
    private Duration maxTravellingTime;
    private double totalDistanceInKM;
    @NotNull(message = "distanceByCarInKM of the trip must be included in request body")
    private double distanceByCarInKM;
    @NotNull(message = "minTripCharges of the trip must be included in request body")
    private double minTripCharges;
    private double maxTripCharges;
    private int minCapacityInKilos;
    private int maxCapacityInKilos;

    //@ManyToMany(mappedBy = "trips", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    //Set<StoreEntity> stores = new HashSet<>();

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

   // @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "stores")   //TODO: Detta??
   // private StoreEntity storEntity;

    //TODO: implementera hashcode och equals metoder??
}
