package iths.glenn.drick.service;

import iths.glenn.drick.entity.ResultEntity;

import java.util.List;

public interface CalculationsService {
    List<ResultEntity> priceForDrunks(String username, int drunks, int amount);
    List<ResultEntity> drunksForBudget(String username, int budget, int amount);
    List<ResultEntity> drunksToBeWorthIt(String username);
    List<ResultEntity> drunksForBudgetByType(String username, int budget, int amount, String type);
}
