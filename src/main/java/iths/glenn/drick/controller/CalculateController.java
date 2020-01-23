package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.ResultEntity;
import iths.glenn.drick.entity.UserEntity;
import iths.glenn.drick.service.CalculationsService;
import iths.glenn.drick.service.CalculationsServiceImplementation;
import iths.glenn.drick.service.DrinksService;
import iths.glenn.drick.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calculate")
public class CalculateController {
    @Autowired
    CalculationsService calculator;

    @Autowired
    DrinksService drinksService;

    @Autowired
    TripService tripService;

    @GetMapping("/drunkPrice/user/{username}/drunks/{drunks}/fetchAmount/{fetchAmount}")
    public List<ResultEntity> drunkPrice(@PathVariable String username, @PathVariable int drunks, @PathVariable int fetchAmount){
        return calculator.priceForDrunks(username, drunks, fetchAmount);
    }

    @GetMapping("/drunksForBudget/user/{username}/budget/{budget}/fetchAmount/{fetchAmount}")
    public List<ResultEntity> drunksForBudget(@PathVariable String username, @PathVariable int budget, @PathVariable int fetchAmount){
        return calculator.drunksForBudget(username, budget, fetchAmount);
    }

    @GetMapping("/drunksForBudget/user/{username}/budget/{budget}/fetchAmount/{fetchAmount}/type/{type}")
    public List<ResultEntity> drunksForBudget(@PathVariable String username, @PathVariable int budget, @PathVariable int fetchAmount, @PathVariable String type){
        return calculator.drunksForBudgetByType(username, budget, fetchAmount, type);
    }
/*
    @GetMapping("cheapestDrunks/{drunks}")
    public List<ResultEntity> cheapestDrunks(UserEntity user, @PathVariable int drunks){
        List<ResultEntity> results = new ArrayList<>();
        List<DrinkEntity> top10Drinks = drinksService.findAmountBestApkFromEachStore(10);

        for(int i = 0; i < top10Drinks.size(); i++){
            ResultEntity result = new ResultEntity();
            double price = calculator.priceToGetDrunk(top10Drinks.get(i), drunks, user.getWeight());

            result.setPriceToGetDrunk(price);
            result.setDrinkName(top10Drinks.get(i).getName());
            results.add(result);
        }
        return results;
    }

    @GetMapping("drunksForPrice/{price}")
    public List<ResultEntity> drunksForPrice(UserEntity user, @PathVariable double price){
        List<ResultEntity> results = new ArrayList<>();
        List<DrinkEntity> top10Drinks = drinksService.findAmountBestApkFromEachStore(10);

        for(int i = 0; i < top10Drinks.size(); i++){
            ResultEntity result = new ResultEntity();
            int drunks = calculator.amountOfDrunksForPrice(top10Drinks.get(i), price, user.getWeight());

            result.setAmountOfDrunksForPrice(drunks);
            results.add(result);
        }
        return results;
    }

    @GetMapping("drunksForPriceWithTrip/{price}/{tripDistance}/{fuelConsumption}")
    public List<ResultEntity> drunksForPriceWithTrip(UserEntity user, @PathVariable double price,
                                                     @PathVariable double tripDistance, @PathVariable double fuelConsumption){
        price -= priceForTrip(tripDistance, fuelConsumption);

        return drunksForPrice(user, price);
    }*/
}
