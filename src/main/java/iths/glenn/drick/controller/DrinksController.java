package iths.glenn.drick.controller;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.repository.DrinkStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinksController {

    DrinkStorage drinkStorage;

    public DrinksController(DrinkStorage drinkStorage) {
        this.drinkStorage = drinkStorage;
    }

    @GetMapping("")
    public List<DrinkEntity> getAll() {
        System.err.println("Hello, World!");
        return drinkStorage.findAll();
    }
}
