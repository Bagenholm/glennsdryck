package iths.glenn.drick.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "drinks")
@NoArgsConstructor
public class DrinkEntity implements Serializable {
    static final long serialVersionUID = 1L;
    private @Id String drinkKey;

    //Mapped by stores
    @Column(name = "store")
    //@ManyToOne
    //@JoinColumn(name = "store_name")
    StoreEntity store;
    String storeName;
    
    @Column(name = "productname")
    String name;
    @Column(name = "category")
    String type;
    @Column(name = "subcategory")
    String subtype;
    float volume; // in millilitres
    float pricePerLitre, alcohol;
    float price, alcoholPerPrice; // how many ml 100% alc for 1 crown?

    public DrinkEntity(String name, String type, String subtype, float price, float pricePerLitre, float alcohol, float volume, StoreEntity storeEntity) {
        this.name = name;
        this.subtype = subtype;
        this.type = type;
        this.pricePerLitre = pricePerLitre;
        this.alcohol = alcohol;
        this.volume = volume;
        this.price = price;
        this.store = storeEntity;
        this.storeName = storeEntity.getStoreName();
        this.alcoholPerPrice = (alcohol / pricePerLitre) * 10; // in ml (alcohol is already * 100)
        this.drinkKey = storeEntity.getStoreName() + "-" + name + "-" + volume + "-" + alcohol;
    }

    public float getAlcoholPerPrice() {
        return alcoholPerPrice;
    }

    public String getName() {
        return name;
    }

    public StoreEntity getStore() { return store; }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
        this.alcoholPerPrice = alcohol / pricePerLitre;
    }

    public float getAlcohol() {
        return alcohol;
    }
}
