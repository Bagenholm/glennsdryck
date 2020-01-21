package iths.glenn.drick.scraper;

import iths.glenn.drick.entity.DrinkEntity;

import java.io.IOException;
import java.util.List;

public interface ScraperService {
    List<DrinkEntity> start() throws IOException;
}
