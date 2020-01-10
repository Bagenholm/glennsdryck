package iths.glenn.drick.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@Entity
@Table(name = "stores")
@NoArgsConstructor
public class StoreEntity implements Serializable {
    static final long serialVersionUID = 1L;
    @Id String storeName;
    String currency;
    @JsonIgnore ArrayList<DrinkEntity> drinks;
    Date lastDateScraped;

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
}
