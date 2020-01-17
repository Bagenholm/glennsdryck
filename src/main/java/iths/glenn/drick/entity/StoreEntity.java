package iths.glenn.drick.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.management.InstanceAlreadyExistsException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Date;

@Data
@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
public class StoreEntity implements Serializable {
    static final long serialVersionUID = 1L;
    @Id String storeName;
    String currency;
    @JsonIgnore ArrayList<DrinkEntity> drinks;
    Instant instanceLastScraped = Instant.EPOCH;

    public String getStoreName() {
        return storeName;
    }

    public ArrayList<DrinkEntity> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<DrinkEntity> drinks) {
        this.drinks = drinks;
    }

    public StoreEntity(String storeName, String currency) {
        this.storeName = storeName;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public long getSecondsSinceLastScraped() {
        return Duration.between(Instant.now(), instanceLastScraped).getSeconds();
    }

    public boolean isScrapedRecently() {
        return Math.abs(Duration.between(Instant.now(), instanceLastScraped).toDays()) < 1;
    }

    public void setInstanceLastScrapedToNow() {
        this.instanceLastScraped = Instant.now();
    }
}
