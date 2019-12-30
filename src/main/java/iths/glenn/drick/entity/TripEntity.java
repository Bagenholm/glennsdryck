package iths.glenn.drick.entity;

import iths.glenn.drick.trip.TripDistance;

import java.util.List;

public class TripEntity {

    String startPoint;
    String endPoint;
    List<TripDistance> tripDistances;
    String tripInfo;
    double fuelConsumptionLitrePerMile;
    double totalPrice;
    double capacityInLitre;    //TODO: kapasitet i vikt?
}
