package iths.glenn.drick.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;

@NoArgsConstructor
@Getter
@Setter
public class TripResultEntity {
    private @Id @GeneratedValue long id;

    double totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Duration minTravelTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Duration maxTravelTime;
    String travelDescription;

    public TripResultEntity(TripEntity tripEntity, long totalFuelPrice){
        totalPrice = tripEntity.getMinTripCharges() * 2 + totalFuelPrice;
        minTravelTime = tripEntity.getMinTravellingTime();
        maxTravelTime = tripEntity.getMaxTravellingTime();
        travelDescription = getTravelDescription(tripEntity);
    }

    private String getTravelDescription(TripEntity trip){
        return "To: " + trip.getTripId().getEndPoint() + ", Transportation: " + trip.getTripId().getWayOfTravel() +
                ", Other: " + trip.getTripId().getTripInfo();
    }
}
