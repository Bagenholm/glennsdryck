package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.StoreEntity;
import iths.glenn.drick.repository.DrinkStorage;
import iths.glenn.drick.repository.StoreStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DriveinbottleshopScraper implements ScraperService {

    DrinkStorage drinkStorage;
    StoreStorage storeStorage;
    StoreEntity driveinbottleshop;

    float currencyExchangeRate;

    public DriveinbottleshopScraper(DrinkStorage drinkStorage, StoreStorage storeStorage) {
        this.drinkStorage = drinkStorage;
        this.storeStorage = storeStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {
        driveinbottleshop = storeStorage.findById("driveinbottleshop")
                .orElse(new StoreEntity("driveinbottleshop", "DKK"));
        currencyExchangeRate = CurrencyExchangeRateService.exchangeRate(driveinbottleshop.getCurrency());

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();
        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .filter(drinkEntity -> !Float.isNaN(drinkEntity.getAlcoholPerPrice()))
                .collect(Collectors.toList());

        driveinbottleshop.setDrinks(filteredDrinks);

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));

        return filteredDrinks;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        drinks.addAll(scrapeDrinks("Öl", "Mörk öl", "http://driveinbottleshop.dk/category/ol-cider/mork-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Ljus öl", "http://driveinbottleshop.dk/category/ol-cider/ljus-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "http://driveinbottleshop.dk/category/ol-cider/veteol/"));
        drinks.addAll(scrapeDrinks("Alkoläsk och Cider", "", "http://driveinbottleshop.dk/category/ol-cider/alkolask-cider/"));
        drinks.addAll(scrapeDrinks("Mousserande vin", "Mousserande", "http://driveinbottleshop.dk/category/vin/mousserande-viner/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/argentina/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/australien/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/chile/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/frankrike/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/italien/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/nya-zeeland/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/osterrike-roda-viner/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/portugal/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/spanien/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/sydafrika/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/tyskland-roda-viner/"));
        drinks.addAll(scrapeDrinks("Rött vin", "Rött", "http://driveinbottleshop.dk/category/vin/roda-viner/usa/"));
        drinks.addAll(scrapeDrinks("Rosévin", "Rosé", "http://driveinbottleshop.dk/category/vin/rose-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/argentina-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/australien-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/chile-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/frankrike-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/italien-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/nya-zealand/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/osterrike/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/spanien-portugal/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/sydafrika-vita-viner/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/tyskland/"));
        drinks.addAll(scrapeDrinks("Vitt vin", "Vitt", "http://driveinbottleshop.dk/category/vin/vita-viner/usa-vita-viner/"));
        drinks.addAll(scrapeDrinks("Anissprit", "Absint", "http://driveinbottleshop.dk/category/sprit/absint/"));
        drinks.addAll(scrapeDrinks("Aperitif och dessert", "", "http://driveinbottleshop.dk/category/sprit/apertif-digestif/"));
        drinks.addAll(scrapeDrinks("Sprit", "Bitter", "http://driveinbottleshop.dk/category/sprit/bitter/"));
        drinks.addAll(scrapeDrinks("Sprit", "Kryddad snaps", "http://driveinbottleshop.dk/category/sprit/brannvin/akvavit-ovrig-kryddad-snaps/"));
        drinks.addAll(scrapeDrinks("Vodka", "Smaksatt", "http://driveinbottleshop.dk/category/sprit/brannvin/smaksatt-vodka/"));
        drinks.addAll(scrapeDrinks("Sprit", "Cognac", "http://driveinbottleshop.dk/category/sprit/cognac-calvados-brandy/"));
        drinks.addAll(scrapeDrinks("Sprit", "Gin", "http://driveinbottleshop.dk/category/sprit/gin/"));
        drinks.addAll(scrapeDrinks("Sprit", "Grappa", "http://driveinbottleshop.dk/category/sprit/grappa/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/citruslik%c3%b8r/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/cremelikor/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/frukt-och-barlikor/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/kakao-och-kaffelikor/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/n%c3%b8tlik%c3%b8r/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/ortlikor/"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "http://driveinbottleshop.dk/category/sprit/likor/whiskylik%c3%b8r/"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "http://driveinbottleshop.dk/category/sprit/rom/mork-rom/"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "http://driveinbottleshop.dk/category/sprit/rom/smaksatt-rom/"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "http://driveinbottleshop.dk/category/sprit/rom/vit-rom/"));
        drinks.addAll(scrapeDrinks("Sprit", "Tequila", "http://driveinbottleshop.dk/category/sprit/tequila/"));
        drinks.addAll(scrapeDrinks("Sprit", "Vodka", "http://driveinbottleshop.dk/category/sprit/vodka/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/irlandsk-whisky/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/japansk-whisky/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/kanadensisk-whisky/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/malt-whisky/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/skotsk-blended/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/svensk-whisky/"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "http://driveinbottleshop.dk/category/sprit/whisky/whisky-fran-usa/"));

        return drinks;
    }

    /* private void getElementsByTextForHtmlParse(String s) throws IOException {
        Document doc;
        doc = Jsoup.connect(s).get();
        Elements articles = doc.getElementsByClass("product-search");

        System.out.println(articles);
    }

    private ArrayList<DrinkEntity> scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("<article class=\"product-search\">\n" +
                " <a class=\"thumbnail\" title=\"Amarula\" href=\"http://driveinbottleshop.dk/?attachment_id=70844\" style=\"background-image: url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/Amarula-110x130.png)\"></a>\n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=amarula-70-cl\">Amarula Cream 70 cl.</a></h3>\n" +
                "  <h4>109.95 DKK</h4>\n" +
                "  <p>Krämig likör, 17 % alk. 70 cl, Sydafrika En cremelikör som är smaksatt med den exotiska Amarulafrukten.... <a href=\"http://driveinbottleshop.dk/?produkt=amarula-70-cl\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65124\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article");

        Elements articles = doc.getElementsByClass("product-search");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        articles.forEach(article -> {
            System.err.println(article.getElementsByTag("p").text() + "\n" +
                    "Name: " + extractNameFromText(article) + "\n" +
                    "Price: " + extractPriceFromText(article) + "\n" +
                    "Alcohol: " + extractAlcoholFromText(article) + "\n" +
                    "Volume: " + extractVolumeFromText(article) );
            drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;
    } */

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements articles = doc.getElementsByClass("product-search");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> {
            drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;

    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        String name = extractNameFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article) * 10;
        float price = extractPriceFromText(article) * currencyExchangeRate;

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, driveinbottleshop);
    }

    private String extractNameFromText(Element article) {
        return article.getElementsByTag("h3").text();
    }

    private float extractPriceFromText(Element article) {
        String priceString = article.getElementsByTag("h4").text();
        return Float.parseFloat(priceString.replaceAll("[a-öA-Ö, ]", ""));
    }

    private float extractAlcoholFromText(Element article) {
        String alkoholhalt = article.getElementsByTag("p").text();
        int percIndex = alkoholhalt.indexOf("%");
        int minIndex = percIndex-6;
        if(minIndex >= 0) {
            String substring = alkoholhalt.substring(minIndex, percIndex + 1)
                    .replaceAll("[a-zA-Z %]", "")
                    .replaceAll("^[,|.]", "")
                    .replace(",", ".");
            if(substring.equals("100")) { //Prevents odd cases where 100% is before alcohol%, they're all 12.5.
                return 12.5f;
            }
            try { return Float.parseFloat(substring);
            } catch (NumberFormatException e) {
                return 0f;
            }
        }
        return 0f;
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByTag("p").text();
        int clCommaIndex = volumeString.indexOf("cl,");
        int minIndex = clCommaIndex - 6;
        if(minIndex >= 0) {
            String substring = volumeString.substring(minIndex, clCommaIndex);
            if(isMultiPack(volumeString, substring, article)) {
                if(substring.matches(".*[0-9][x][0-9].*")) { //Prevents 6x75 from being read as 675
                    substring = substring.trim();
                    int numberOfBottles = Integer.parseInt(substring.substring(0,1));
                    int volumePerBottle = Integer.parseInt(substring.substring(2));
                    return numberOfBottles * volumePerBottle + 0f;
                }
                return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", "")) * multiPackMultiplier(volumeString, substring, article);
            }
            if(substring.matches(".*([0-9],[0-9]).*")) {
                substring = substring.replaceFirst(",", ".");
            } if(substring.matches(".*\\D[.].*")) { //If substring has a letter then a period, remove period to prevent 0.X instead of X.
                substring = substring.replace(".", "");
            }
            substring = substring.replaceAll("[a-öA-Ö%, ]", "");
            if(substring.equals("")) { // Catches various typos that results in empty strings.
                return extractVolumeFromTextOddCases(extractNameFromText(article), volumeString);
            }
            return Float.parseFloat(substring);
        } else if(minIndex == -7) {
            int clPeriodIndex = volumeString.indexOf("cl.");
            minIndex = clPeriodIndex - 6;
            if(minIndex >= 0) {
                String substring = volumeString.substring(minIndex, clPeriodIndex);
                return Float.parseFloat(substring.replaceAll("[a-öA-Ö%, ]", ""));
            }
        }
        String name = extractNameFromText(article);
        boolean isclVolumeInName = name.matches(".*\\b[0-9]* cl.*");
        boolean ismlVolumeInName = name.matches(".*\\b[0-9]* ml.*");
        if(isclVolumeInName) {
            return (Float.parseFloat(name.substring(name.indexOf(" cl") - 2, name.indexOf(" cl"))));
        } else if(ismlVolumeInName) {
            return (Float.parseFloat(name.substring(name.indexOf(" ml") - 3, name.indexOf(" ml"))) / 10);
        }
        return extractVolumeFromTextOddCases(name, volumeString);
    }

    public float extractVolumeFromTextOddCases(String name, String volumeString) {
        if(name.contains("5 L")) {
            return 500f;
        } else if(name.toLowerCase().contains("1 liter") || volumeString.toLowerCase().contains("1 liter")) {
            return 100f;
        } else if(volumeString.toLowerCase().contains("3 liter")) {
            return 300f;
        } else if(volumeString.toLowerCase().contains("0,7 liter")) {
            return 70f;
        }
        return 0f;
    }

    private int multiPackMultiplier(String volumeString, String substring, Element article) {
        if(volumeString.contains("24") || extractNameFromText(article).contains("24 burkar")) {
            return 24;
        } else if(volumeString.contains("6") || extractNameFromText(article).contains("6 flaskor") || extractNameFromText(article).endsWith("6 st")) {
            return 6;
        } else if(substring.contains("3x2")) {
            return 3;
        }
        return 1;
    }

    private boolean isMultiPack(String volume, String substring, Element article) {
        if(substring.contains("x")) {
            if(volume.contains("24")) {
                return true;
            } else if(volume.contains("6")) {
                return true;
            } else if(volume.contains("3x2")) {
                return true;
            }
        } else if(extractNameFromText(article).contains("24 burkar")) {
            return true;
        } else if(extractNameFromText(article).contains("6 flaskor")) {
            return true;
        } else if(extractNameFromText(article).endsWith("6 st")) {
            return true;
        }
        return false;
    }

}