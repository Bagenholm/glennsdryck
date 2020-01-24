package iths.glenn.drick.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trip")
//@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TripEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private static final long serialVersionUID = 1L;

    private TripId tripId;
    @Column(name = "trip_city")
    private String city;

   // @Pattern(message = "Wrong format of duration for minTravellingTime", regexp = "PTnHnM")  //TODO: Ersätta regex mot nåt programmet kan hantera, bokstäver och siffror typ
    private Duration minTravellingTime;
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

    @ManyToMany(mappedBy = "trips", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<StoreEntity> stores = new HashSet<>();


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

        this.city = tripId.getEndPoint();

    }

    @PreRemove
    public void removeTripFromStores() {

        for (StoreEntity storeEntity : stores) {
            storeEntity.getTrips().remove(this);
        }
    }

    public void addStore(StoreEntity storeEntity) {

        this.stores.add(storeEntity);
       // storeEntity.getTrips().add(this);
    }

    public void removeStore(StoreEntity storeEntity) {

        this.stores.remove(storeEntity);
        storeEntity.getTrips().remove(this);
    }
}
