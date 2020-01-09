package iths.glenn.drick.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "stores")
@NoArgsConstructor
public class StoreEntity {
    @Id
    private String name;
    String currency;
    ArrayList<DrinkEntity> drinks;
    Date lastDateScraped;

    public String getName() {
        return name;
    }

    public ArrayList<DrinkEntity> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<DrinkEntity> drinks) {
        this.drinks = drinks;
    }

    public StoreEntity(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }
}
