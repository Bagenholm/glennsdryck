package iths.glenn.drick.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DrinkEntity {
    String name;
    String type;
    float pricePerLitre, alcohol;
    ArrayList<StoreEntity> storesList;
}
