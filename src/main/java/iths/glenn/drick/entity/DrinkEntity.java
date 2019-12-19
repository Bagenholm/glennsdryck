package iths.glenn.drick.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DrinkEntity {
    String name;
    String type;
    String subtype;
    float pricePerLitre, alcohol;
    ArrayList<StoreEntity> storesList;

    public DrinkEntity(String name, String type, String subtype, float pricePerLitre, float alcohol) {
        this.name = name;
        this.subtype = subtype;
        this.type = type;
        this.pricePerLitre = pricePerLitre;
        this.alcohol = alcohol;
    }

    public float getAlcoholPerPrice() {
        return alcohol/pricePerLitre;
    }
}
