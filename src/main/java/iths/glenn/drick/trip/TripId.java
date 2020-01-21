package iths.glenn.drick.trip;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TripId implements Serializable {

    private static final long serialVersionUID = 1L;


    @NotNull(message = "startPoint of the trip must be included in request body")
    @Column(name = "startPoint")
    private String startPoint;
    @NotNull(message = "endPoint of the trip must be included in request body")
    @Column(name = "endPoint")
    private String endPoint;
    @NotNull(message = "tripInfo of the trip must be included in request body")
    @Column(name = "tripInfo")
    private String tripInfo;
    @NotNull(message = "wayOfTravel of the trip must be included in request body")
    @Column(name = "wayOfTravel")
    private WayOfTravel wayOfTravel;

    public TripId() {
    }

    public TripId(String startPoint, String endPoint, String tripInfo, WayOfTravel wayOfTravel) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.tripInfo = tripInfo;
        this.wayOfTravel = wayOfTravel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripId)) return false;
        TripId tripId = (TripId) o;
        return startPoint.equals(tripId.startPoint) &&
                endPoint.equals(tripId.endPoint) &&
                tripInfo.equals(tripId.tripInfo) &&
                wayOfTravel == tripId.wayOfTravel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint, tripInfo, wayOfTravel);
    }
}
