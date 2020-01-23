package iths.glenn.drick.controller;

import iths.glenn.drick.entity.ResultEntity;
import iths.glenn.drick.service.CalculationsService;
import iths.glenn.drick.service.DrinksService;
import iths.glenn.drick.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.NotAuthorizedException;
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
        try {
            return calculator.priceForDrunks(username, drunks, fetchAmount);
        }
        catch(NotAuthorizedException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized", e);
        }
        catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal argument", e);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endpoint error", e);
        }
    }

    @GetMapping("/drunksForBudget/user/{username}/budget/{budget}/fetchAmount/{fetchAmount}")
    public List<ResultEntity> drunksForBudget(@PathVariable String username, @PathVariable int budget, @PathVariable int fetchAmount){
        try {
            return calculator.drunksForBudget(username, budget, fetchAmount);
        }
        catch(NotAuthorizedException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized", e);
        }
        catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal argument", e);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endpoint error", e);
        }
    }

    @GetMapping("/drunksForBudget/user/{username}/budget/{budget}/fetchAmount/{fetchAmount}/type/{type}")
    public List<ResultEntity> drunksForBudget(@PathVariable String username, @PathVariable int budget, @PathVariable int fetchAmount, @PathVariable String type){
        return calculator.drunksForBudgetByType(username, budget, fetchAmount, type);
    }
}
