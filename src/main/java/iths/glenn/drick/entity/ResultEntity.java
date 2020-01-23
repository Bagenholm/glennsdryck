package iths.glenn.drick.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ResultEntity {
    private @Id @GeneratedValue long id;

    double totalPrice;
    String drinkName;
    double drinkPrice;
    double priceToGetDrunk;
    double totalDrunks;
    int amountOfDrunksForPrice;
    double cheapestTravelPrice;
    double cheapestTravelTime;
    double totalDrinkVolume;
    String store;
    List<TripResultEntity> tripOptions = new ArrayList<>();
}
