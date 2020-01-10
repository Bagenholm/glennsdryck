package iths.glenn.drick.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "drinks")
@NoArgsConstructor
public class DrinkEntity implements Serializable {
    static final long serialVersionUID = 1L;
    private @Id String drinkKey;

    //Mapped by stores
    ArrayList<String> storesList = new ArrayList<>();
    @Column(name = "productname")
    String name;
    @Column(name = "category")
    String type;
    @Column(name = "subcategory")
    String subtype;
    float volume; // in millilitres
    float pricePerLitre, alcohol;
    float price, alcoholPerPrice;

    public DrinkEntity(String name, String type, String subtype, float price, float pricePerLitre, float alcohol, float volume, StoreEntity storeEntity) {
        this.name = name;
        this.subtype = subtype;
        this.type = type;
        this.pricePerLitre = pricePerLitre;
        this.alcohol = alcohol;
        this.volume = volume;
        this.price = price;
        this.storesList.add(storeEntity.storeName);
        this.alcoholPerPrice = alcohol / pricePerLitre;
        this.drinkKey = storeEntity.getStoreName() + "-" + name + "-" + volume + "-" + alcohol;
    }

    public float getAlcoholPerPrice() {
        return alcoholPerPrice;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getStoresList() {
        return storesList;
    }
}
