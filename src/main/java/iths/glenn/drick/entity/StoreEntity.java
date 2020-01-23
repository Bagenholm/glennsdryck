package iths.glenn.drick.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity implements Serializable {

    static final long serialVersionUID = 1L;
    @Column(name = "store_name")
    @Id
    String storeName;
    String currency;
    Instant instanceLastScraped = Instant.EPOCH;
    String city;

    /*@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "stores_drinks",
        joinColumns =@JoinColumn(name = "store_name", referencedColumnName = "store_name"),
        inverseJoinColumns = @JoinColumn(name = "drink_name", referencedColumnName = "productname"))
    Set<DrinkEntity> drinks; */

   /* @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "store_trip",
        joinColumns = @JoinColumn(name = "store_city", referencedColumnName = "city"),
        inverseJoinColumns = @JoinColumns ({
                @JoinColumn(name = "trip_city", referencedColumnName = "endpoint"),
                @JoinColumn(name = "trip_wayoftravel", referencedColumnName = "wayOfTravel"),
                @JoinColumn(name = "trip_tripinfo", referencedColumnName = "tripInfo"),
                @JoinColumn(name = "trip_start", referencedColumnName = "startPoint")
        })
    Set<TripEntity> trips = new HashSet<>();*/

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "store_trip",
            joinColumns = @JoinColumn(name = "store_name", referencedColumnName = "store_name"),
            inverseJoinColumns = @JoinColumn (name = "trip_city", referencedColumnName = "trip_city"))
    private Set<TripEntity> trips = new HashSet<>();


    public StoreEntity(String storeName, String currency, String city) {
        this.storeName = storeName;
        this.currency = currency;
        this.city = city;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isScrapedRecently() {
        return Math.abs(Duration.between(Instant.now(), instanceLastScraped).toDays()) < 1;
    }

    public void setInstanceLastScrapedToNow() {
        this.instanceLastScraped = Instant.now();
    }

    public void addTrip(TripEntity tripEntity) {

        this.trips.add(tripEntity);
        tripEntity.getStores().add(this);
    }

    public void removeTrip(TripEntity tripEntity) {

        this.trips.remove(tripEntity);
        tripEntity.getStores().remove(this);
    }
}