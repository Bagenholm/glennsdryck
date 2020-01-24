package iths.glenn.drick.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ResultEntity {
    @JsonIgnore
    private @Id @GeneratedValue long id;

    double totalPrice;
    String drinkName;
    double drinkPrice;
    double priceToGetDrunk;
    double totalDrunks;
    double cheapestTravelPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Duration cheapestTravelTime;
    double totalDrinkVolume;
    double drinkVolume;
    double alcohol;
    String store;
    List<TripResultEntity> tripOptions = new ArrayList<>();
}
