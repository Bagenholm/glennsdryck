package iths.glenn.drick.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreModel {

    private String storeName;
    private String currency;
    private String city;
    private List<TripModel> trips;
}
