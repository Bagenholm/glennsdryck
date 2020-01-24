package iths.glenn.drick.controller;

import iths.glenn.drick.exception.StoreDontExistException;
import iths.glenn.drick.model.StoreModel;
import iths.glenn.drick.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("")
    public List<StoreModel> listAllStores() {

        try {
            return storeService.listAllStores();
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @GetMapping("/city/{city}")
    public List<StoreModel> listAllStoresInCity(@PathVariable(name = "city") String city) {

        try {
            return storeService.listStoresInCity(city);
        }catch(StoreDontExistException dex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No store was found", dex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @GetMapping("/{storeName}")
    public StoreModel getStoreByName(@PathVariable(name = "storeName") String storeName) {

        try {
            return storeService.getStoreByName(storeName);
        }catch(StoreDontExistException dex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found", dex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @PatchMapping("")
    public List<StoreModel> joinStoresWithTrips() {

        try {
            return storeService.joinStoresWithTrips();
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }
}
