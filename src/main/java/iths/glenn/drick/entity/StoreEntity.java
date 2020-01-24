package iths.glenn.drick.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

//@Data
@Entity
@Table(name = "stores")
@Getter
@Setter
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

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "store_trip",
            joinColumns = @JoinColumn(name = "store_name", referencedColumnName = "store_name"),
            inverseJoinColumns = @JoinColumn (name = "id", referencedColumnName = "id"))
    private Set<TripEntity> trips = new HashSet<>();

    public StoreEntity(String storeName, String currency, String city) {
        this.storeName = storeName;
        this.currency = currency;
        this.city = city;
    }

    public boolean isScrapedRecently() {
        return Math.abs(Duration.between(Instant.now(), instanceLastScraped).toDays()) < 1;
    }

    public void setInstanceLastScrapedToNow() {
        this.instanceLastScraped = Instant.now();
    }

    public void addTrip(TripEntity tripEntity) {

        this.trips.add(tripEntity);
       // tripEntity.getStores().add(this);
    }

    public void removeTrip(TripEntity tripEntity) {

        this.trips.remove(tripEntity);
        tripEntity.getStores().remove(this);
    }
}