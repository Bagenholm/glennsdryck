package iths.glenn.drick.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StenalineScraper implements ScraperService {

    DrinkStorage drinkStorage;
    StoreStorage storeStorage;
    StoreEntity stenaline;

    public StenalineScraper(DrinkStorage drinkStorage, StoreStorage storeStorage) {
        this.drinkStorage = drinkStorage;
        this.storeStorage = storeStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {
        stenaline = storeStorage.findById("stenaline")
                .orElse(new StoreEntity("stenaline", "SEK"));

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();

        drinks.forEach(drink -> extractAndSetAlcoholFromOtherStores(drink));

        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .collect(Collectors.toList());

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));

        return filteredDrinks;

        //return drinks;

       // scrapeDrinksTest("http://shopping.stenaline.se/api/search?CMS_SearchString=&Id=ddefd7a0885f494a915638bbb6759bfe&cid=2b128b55-93f8-4cf8-8653-88a6d420efa8&ProductSetId=00000000-0000-0000-0000-000000000000&FromPrice=0&ToPrice=0", "Öl", "");
    }

    private void extractAndSetAlcoholFromOtherStores(DrinkEntity drink) {
        List<DrinkEntity> resultDrinks = drinkStorage.findByPartialNameNotStena(drink.getName());
        if(resultDrinks.size() > 0) {
            drink.setAlcohol(resultDrinks.get(0).getAlcohol());
        }
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Öl", "", "http://shopping.stenaline.se/api/search?CMS_SearchString=&Id=ddefd7a0885f494a915638bbb6759bfe&cid=2b128b55-93f8-4cf8-8653-88a6d420efa8&ProductSetId=00000000-0000-0000-0000-000000000000&FromPrice=0&ToPrice=0"));
        drinks.addAll(scrapeDrinks("Vin", "", "http://shopping.stenaline.se/api/search?CMS_SearchString=&Id=ddefd7a0885f494a915638bbb6759bfe&cid=7347a5b8-1a92-4151-93ea-1e273801c9d8&ProductSetId=00000000-0000-0000-0000-000000000000&FromPrice=0&ToPrice=0"));
        drinks.addAll(scrapeDrinks("Whisky", "", "http://shopping.stenaline.se/api/search?CMS_SearchString=&Id=ddefd7a0885f494a915638bbb6759bfe&cid=ed2ea570-4102-4caa-8b10-0acd68c937d0&ProductSetId=00000000-0000-0000-0000-000000000000&FromPrice=0&ToPrice=0"));
        drinks.addAll(scrapeDrinks("Övrig sprit", "", "http://shopping.stenaline.se/api/search?CMS_SearchString=&Id=ddefd7a0885f494a915638bbb6759bfe&cid=17bda0d6-bde5-449d-9d5d-d31183b85581&ProductSetId=00000000-0000-0000-0000-000000000000&FromPrice=0&ToPrice=0"));

        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        Document doc;
        doc = Jsoup.connect(url).ignoreContentType(true).get();
        String jsonString = doc.getElementsByTag("body").text();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString).get("products");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        jsonNode.forEach(node -> drinks.add(makeDrink(node, type, subtype)));

        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinksTest(String url, String type, String subtype) throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        Document doc;
        doc = Jsoup.connect(url).ignoreContentType(true).get();
        String jsonString = doc.getElementsByTag("body").text();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString).get("products");

        jsonNode.forEach(node -> {
            System.err.println(node.toPrettyString() + "\n" +
                    "Name: " + extractNameFromText(node) + "\n" +
                    "Price: " + extractPriceFromText(node) + "\n" +
                    "Volume: " + extractVolumeFromText(node) );
            drinks.add(makeDrink(node, "Öl", ""));
        });

        return drinks;
    }

    private DrinkEntity makeDrink(JsonNode node, String type, String subtype) {
        String name = extractNameFromText(node);
        float volume = extractVolumeFromText(node) * 1000;
        float price = extractPriceFromText(node);

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, 0f, volume, stenaline);
    }

    private float extractVolumeFromText(JsonNode node) {
        String volumeString = node.get("size").asText();
        int packMultiplier = 1;
        int xIndex = volumeString.indexOf('x');
        if(xIndex > 0) {
            packMultiplier = extractMultiPackMultiplier(volumeString);
            volumeString = volumeString.substring(xIndex);
        }
        volumeString = volumeString
                .replaceAll(",", ".")
                .replaceAll("[^0-9.]", "");
        return Float.parseFloat(volumeString) * packMultiplier;
    }

    private int extractMultiPackMultiplier(String volumeString) {
        int xIndex = volumeString.indexOf('x');
        String startToX = volumeString.substring(0, xIndex).trim();
        return Integer.parseInt(startToX);
    }

    private String extractNameFromText(JsonNode node) {
        return (node.get("brand").asText() + " " + node
                .get("name").asText()).trim();
    }

    private float extractPriceFromText(JsonNode node) {
        String priceString = node.get("price").asText();
        priceString = priceString.replaceAll("[^0-9]", "");
        return Float.parseFloat(priceString);
    }



}
