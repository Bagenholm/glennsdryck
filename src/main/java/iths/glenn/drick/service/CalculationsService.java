package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;

public class CalculationsService {
    public double priceToGetDrunk(DrinkEntity drink, int amountOfDrunks, double userWeight){
        double kronorToSpend = 0;

        double apk = drink.getPricePerLitre() * (drink.getAlcohol() / 100); // ml alcohol per krona
        double gramAlcoholPerPrice = apk * 0.8;
        double promillePerPrice = gramAlcoholPerPrice / userWeight * 0.65;
        System.out.println(promillePerPrice);

        for(double i = 0; i < amountOfDrunks; i += promillePerPrice){
            kronorToSpend++;
        }
        return kronorToSpend;
    }

    // Dra av för resa
    public int amountOfDrunksForPrice(DrinkEntity drink, double price, double userWeight){
        int drunks = 0;

        while(price > priceToGetDrunk(drink, 1, userWeight)){
            price -= priceToGetDrunk(drink, 1, userWeight);
            drunks++;
        }
        return drunks;
    }

    public double fuelConsumptionForTrip(double distanceInKilometer, double fuelConsumptionPerMile){
        return fuelConsumptionPerMile * distanceInKilometer * 10;
    }

    /*
    Kvinna: Alkohol i g/(kroppsvikten i kg x 60 %) - (0,15 x timmar från intagets början) = promille
    Man:    Alkohol i g/(kroppsvikten i kg x 70 %) - (0,15 x timmar från intagets början) = promille

    Man:    if(Alkohol i ML = din vikt * 0.875) = 1 promille
    Kvinna: if(Alkohol i ML = din vikt * 0.75) = 1 promille

    Basic test formula: Gram alkohol / 52 = promille

    ML alkohol * 0.8 = gram alkohol

    Vodka shot = 6 cl, 40%
    24 ml alkohol / vodka shot.
    19.2 g alkohol / vodka shot.
    0.36923076923 promille / vodka shot.

    (mängdmått) Alkohol Per Krona

    APK = ((mängdmått) mängd * alcohol) / pris
    APK = (alcohol / pris) * (mängdmått) mängd
    APK = ((mängdmått) mängd / pris *) alcohol

    1 liter dryck, 100 kr, 10%
    0.10 / 100 = 0.001 liter APK

    1 deciliter dryck, 10 kr, 10%
    0.10 / 10 kr = 0.01 dciliter APK
    */
}