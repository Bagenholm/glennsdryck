package iths.glenn.drick.service;

import iths.glenn.drick.entity.*;
import iths.glenn.drick.repository.TripStorage;
import iths.glenn.drick.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalculationsServiceImplementation implements CalculationsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DrinksService drinksService;

    @Autowired
    TripStorage tripStorage;

    static final int FUEL_PRICE = 16;

    @Override
    public List<ResultEntity> priceForDrunks(String username, int drunks, int fetchAmount) {
        UserEntity user = userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("No such user"));
        List<DrinkEntity> drinkList = drinksService.findAmountBestApkFromEachStore(fetchAmount);
        List<ResultEntity> resultList = new ArrayList<>();

        drinkList.forEach(drink -> {
            resultList.add(makeResult(user, drink));
        });

        return resultList;
    }

    @Override
    public List<ResultEntity> drunksForBudget(String username, int budget, int fetchAmount) {
        UserEntity user = userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("No such user"));
        List<DrinkEntity> drinkList = drinksService.findAmountBestApkFromEachStore(fetchAmount);
        List<ResultEntity> resultList = calculateDrunksFromBudget(budget, user, drinkList);

        return resultList.stream()
                .sorted(Comparator.comparing(ResultEntity::getAmountOfDrunksForPrice, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultEntity> drunksToBeWorthIt(String username) {
        return null;
    }

    @Override
    public List<ResultEntity> drunksForBudgetByType(String username, int budget, int fetchAmount, String type) {
        UserEntity user = userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("No such user"));
        List<DrinkEntity> drinkList = drinksService.findAmountBestApkByType(type, fetchAmount);
        List<ResultEntity> resultList = calculateDrunksFromBudget(budget, user, drinkList);

        return resultList.stream()
                .sorted(Comparator.comparing(ResultEntity::getAmountOfDrunksForPrice, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    private List<ResultEntity> calculateDrunksFromBudget(int budget, UserEntity user, List<DrinkEntity> drinkList) {
        List<ResultEntity> resultList = new ArrayList<>();
        for (DrinkEntity drink : drinkList) {
            int drunks = 0;
            ResultEntity result = makeResult(user, drink);

            double drinkBudget = budget - result.getTotalPrice();

            while (result.getPriceToGetDrunk() < drinkBudget) {
                drinkBudget -= result.getPriceToGetDrunk();
                drunks++;
            }
            result.setAmountOfDrunksForPrice(drunks);
            if (result.getAmountOfDrunksForPrice() == 0) {
                continue;
            }
            result.setTotalPrice(result.getPriceToGetDrunk() * drunks);
            resultList.add(result);
        }
        return resultList;
    }

    private ResultEntity makeResult(UserEntity user, DrinkEntity drink){
        ResultEntity result = new ResultEntity();
        List<TripEntity> trips = tripStorage.findAll();
        trips = trips.stream().filter(trip -> trip.getCity().equals(drink.getStore().getCity())).collect(Collectors.toList()); //Fullösning. Ska hämta direkt från tripStorage, men vill inte. Varför?
        //Set<TripEntity> trips = tripStorage.findAllByCityContains(drink.getStore().getCity());

        double apk = drink.getAlcoholPerPrice(); // ml alcohol per krona
        double alcoholForOnePromille = user.getWeight() * 0.875;
        double price = alcoholForOnePromille / apk;

        trips.forEach((trip) -> {
            long fuelConsumption = fuelConsumptionPriceForTrip(trip.getDistanceByCarInKM(), user.getFuelConsumptionRate());
            result.getTripOptions().add(makeTripResult(trip, fuelConsumption));
        });
        // Sort
        result.setTripOptions(
                result.getTripOptions().stream()
                        .sorted(Comparator.comparing(TripResultEntity::getTotalPrice))
                        .collect(Collectors.toList()));

        result.setDrinkName(drink.getName());
        result.setPriceToGetDrunk(price);
        result.setDrinkPrice(drink.getPrice());
        result.setStore(drink.getStoreName());
        result.setTotalPrice(result.getTripOptions().get(0).getTotalPrice() + price);

        return result;
    }

    private TripResultEntity makeTripResult(TripEntity trip, long totalFuelPrice){
        return new TripResultEntity(trip, totalFuelPrice);
    }

    public long fuelConsumptionPriceForTrip(double distanceInKilometer, double fuelConsumptionPerMile){
        return (long) (fuelConsumptionPerMile * (distanceInKilometer / 10)) * 2 * FUEL_PRICE;
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