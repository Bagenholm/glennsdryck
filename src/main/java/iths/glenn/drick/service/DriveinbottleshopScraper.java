package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DriveinbottleshopScraper implements ScraperService {
    @Override
    public List<DrinkEntity> scrape() throws IOException {

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();


        //ArrayList<DrinkEntity> drinks = new ArrayList<>();
        //drinks = scrapeDrinksTest("Vin", "Rött vin");
        //getElementsByTextForHtmlParse("http://driveinbottleshop.dk/category/vin/roda-viner/portugal/");
        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        drinks.addAll(scrapeDrinks("Öl", "Mörk öl", "http://driveinbottleshop.dk/category/ol-cider/mork-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Ljus öl", "http://driveinbottleshop.dk/category/ol-cider/ljus-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "http://driveinbottleshop.dk/category/ol-cider/veteol/"));
        drinks.addAll(scrapeDrinks("Alkoläsk och Cider", "", "http://driveinbottleshop.dk/category/ol-cider/alkolask-cider/"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande", "http://driveinbottleshop.dk/category/vin/mousserande-viner/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/argentina/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/australien/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/chile/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/frankrike/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/italien/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/nya-zeeland/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/osterrike-roda-viner/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/portugal/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/spanien/"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "http://driveinbottleshop.dk/category/vin/roda-viner/sydafrika/"));

        return drinks;
    }

    private void getElementsByTextForHtmlParse(String s) throws IOException {
        Document doc;
        doc = Jsoup.connect(s).get();
        Elements articles = doc.getElementsByClass("product-search");

        System.out.println(articles);
    }

    private ArrayList<DrinkEntity> scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Vidigal Reserva\" href=\"http://driveinbottleshop.dk/?produkt=vidigal-reserva\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2012/01/Vidigal_Reserva-83x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=vidigal-reserva\">Vidigal Reserva</a></h3> \n" +
                "  <h4>59.95 DKK</h4> \n" +
                "  <p>Rött fylligt vin, 75 cl, 12,5 % alk, Lisboa, Portugal. Mycket mörk och kraftig färg. Doften är något knuten, men det kan anas dofter av mörka bär och svarta vingummin. Sm,aken är desto gene... <a href=\"http://driveinbottleshop.dk/?produkt=vidigal-reserva\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"68011\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"My Pride Wine Duoro Red\" href=\"http://driveinbottleshop.dk/?produkt=my-pride-wine-duoro-red\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/09/My-Pride-R-d-353x310-148x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=my-pride-wine-duoro-red\">My Pride Wine Duoro Red</a></h3> \n" +
                "  <h4>59.95 DKK</h4> \n" +
                "  <p>Fylligt rött vin, 75 cl, 12,5 % alk, Duoro, Portugal Druvor: &nbsp;Touriga Franca, Touriga Nacional and Tinta Roriz. En generös doft med mogna mörka bär men också kryddiga dofter som peppar, kryd... <a href=\"http://driveinbottleshop.dk/?produkt=my-pride-wine-duoro-red\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"75857\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Vallado Duoro\" href=\"http://driveinbottleshop.dk/?produkt=vallado-duoro\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/11/Vallado-douro-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=vallado-duoro\">Vallado Duoro</a></h3> \n" +
                "  <h4>89.95 DKK</h4> \n" +
                "  <p>Fylligt rött vin, 75 cl, 13,5 % alk, Duoro Portugal Druvor: Touriga Franca, Touriga Nacional, Tinta Roriz En generös doft med fruktiga toner av bland annat svarta vinbär. Smaken är fyllig, kra... <a href=\"http://driveinbottleshop.dk/?produkt=vallado-duoro\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76240\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Vidigal Reserva, 6 st\" href=\"http://driveinbottleshop.dk/?produkt=reserva-vigigal-6-st\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/12/Vidigal_Reserva-83x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=reserva-vigigal-6-st\">Vidigal Reserva, 6 st</a></h3> \n" +
                "  <h4>359.75 DKK</h4> \n" +
                "  <p>Rött fylligt vin, 75 cl, 12,5 % alk, Lisboa, Portugal. Mycket mörk och kraftig färg. Doften är något knuten, men det&nbsp;kan anas dofter&nbsp;av mörka bär och svarta vingummin. Sm,aken är desto ge... <a href=\"http://driveinbottleshop.dk/?produkt=reserva-vigigal-6-st\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"67654\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>");

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
    }

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
        float price = extractPriceFromText(article);

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume);
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
            return Float.parseFloat(substring);
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
            }
            return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", ""));
        } else if(minIndex == -7) {
            int clPeriodIndex = volumeString.indexOf("cl.");
            minIndex = clPeriodIndex - 6;
            if(minIndex >= 0) {
                String substring = volumeString.substring(minIndex, clPeriodIndex);
                return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", ""));
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
        if(name.contains("5 L")) {
            return 500f;
        }
        return 0f;
    }

    private int multiPackMultiplier(String volumeString, String substring, Element article) {
        if(volumeString.contains("24") || extractNameFromText(article).contains("24 burkar")) {
            return 24;
        } else if(volumeString.contains("6") || extractNameFromText(article).contains("6 flaskor") || extractNameFromText(article).endsWith("6 st")) {
            return 6;
        }
        return 1;
    }

    private boolean isMultiPack(String volume, String substring, Element article) {
        if(substring.contains("x")) {
            if(volume.contains("24")) {
                return true;
            } else if(volume.contains("6")) {
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