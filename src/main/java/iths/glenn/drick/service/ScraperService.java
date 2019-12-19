package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;

import java.io.IOException;
import java.util.List;

public interface ScraperService {
    List<DrinkEntity> scrape() throws IOException;
}
