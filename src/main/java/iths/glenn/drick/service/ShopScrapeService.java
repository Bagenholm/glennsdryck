package iths.glenn.drick.service;

import iths.glenn.drick.repository.StoreStorage;
import org.springframework.stereotype.Service;

@Service
public class ShopScrapeService {

    StoreStorage storeStorage;

    public ShopScrapeService(StoreStorage storeStorage) {
        this.storeStorage = storeStorage;
    }


}
