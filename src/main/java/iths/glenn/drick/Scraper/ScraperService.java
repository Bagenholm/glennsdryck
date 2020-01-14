package iths.glenn.drick.Scraper;

import iths.glenn.drick.entity.DrinkEntity;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface ScraperService {
    List<DrinkEntity> start() throws IOException;
}
