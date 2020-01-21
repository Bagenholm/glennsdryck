package iths.glenn.drick.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ResultEntity {
    private @Id

    String drinkKey;
    String drinkName;
    double drinkPrice;
    double priceToGetDrunk;
    int amountOfDrunksForPrice;
    double totalTravelPrice;
    double totalMaxTravelTime;
    double totalMinTravelTime;
    double totalDrinkVolume;
    String travelDescription;
    String store;
}
