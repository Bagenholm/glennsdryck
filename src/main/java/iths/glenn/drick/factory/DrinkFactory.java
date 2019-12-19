package iths.glenn.drick.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.model.DrinkModel;

public class DrinkFactory {

    ObjectMapper objectMapper = new ObjectMapper();

    public DrinkEntity makeDrinkEntity(String object) throws JsonProcessingException {
        return objectMapper.readValue(object, DrinkEntity.class);
    }

    public DrinkModel makeDrinkModelFromEntity(DrinkEntity drink) {
        DrinkModel newDrink = new DrinkModel();
        newDrink.setName(drink.getName());
        newDrink.setAlcohol(drink.getAlcohol());
        newDrink.setPricePerLitre(drink.getPricePerLitre());
        newDrink.setType(drink.getType());

        return newDrink;
    }
}
