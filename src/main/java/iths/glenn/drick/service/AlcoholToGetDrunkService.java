package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;

public class AlcoholToGetDrunkService {
    public double priceToGetDrunk(DrinkEntity drink, int amountOfDrunks){
        double kronorToSpend = 0;

        float apk = drink.getAlcoholPerPrice() * 1000;// Get MillilitreAlcoholPerPrice
        double gramAlcoholPerPrice = apk * 0.8;
        double promillePerPrice = gramAlcoholPerPrice / 52;

        for(double i = 0; i < amountOfDrunks; i += promillePerPrice){
            kronorToSpend++;
        }
        return kronorToSpend;
    }

    public int amountOfDrunksForPrice(DrinkEntity drink, double price){
        int drunks = 0;

        while(price > priceToGetDrunk(drink, 1)){
            price -= priceToGetDrunk(drink, 1);
            drunks++;
        }
        return drunks;
    }

    /*
    Kvinna: Alkohol i g/(kroppsvikten i kg x 60 %) - (0,15 x timmar från intagets början) = promille
    Man:    Alkohol i g/(kroppsvikten i kg x 70 %) - (0,15 x timmar från intagets början) = promille

    Basic test formula: Gram alkohol / 52 = promille

    ML alkohol * 0.8 = gram alkohol


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
