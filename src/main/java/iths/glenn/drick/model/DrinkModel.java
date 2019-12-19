package iths.glenn.drick.model;

import iths.glenn.drick.entity.StoreEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Data
public class DrinkModel {
    String name;
    String type;
    float pricePerLitre, alcohol;
    ArrayList<StoreModel> storesList;

}
