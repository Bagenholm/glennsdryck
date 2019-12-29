package iths.glenn.drick.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

@Data
@Entity
@NoArgsConstructor
public class DrinkEntity {
    private @Id String key;

    ArrayList<StoreEntity> storesList;
    String name;
    String type;
    String subtype;
    float volume; // in millilitres
    float pricePerLitre, alcohol;

    public DrinkEntity(String name, String type, String subtype, float pricePerLitre, float alcohol, float volume) {
        this.name = name;
        this.subtype = subtype;
        this.type = type;
        this.pricePerLitre = pricePerLitre;
        this.alcohol = alcohol;
        this.volume = volume;
        this.key = name + volume + alcohol;
    }

    public float getAlcoholPerPrice() {
        return alcohol/pricePerLitre;
    }
}
