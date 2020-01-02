package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class DriveinbottleshopScraper implements ScraperService {
    @Override
    public List<DrinkEntity> scrape() throws IOException {

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();


        //ArrayList<DrinkEntity> drinks = new ArrayList<>();

        //scrapeDrinksTest("Vin", "Mousserande");
        //getElementsByTextForHtmlParse("http://driveinbottleshop.dk/category/vin/mousserande-viner/");
        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        drinks.addAll(scrapeDrinks("Öl", "Mörk öl", "http://driveinbottleshop.dk/category/ol-cider/mork-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Ljus öl", "http://driveinbottleshop.dk/category/ol-cider/ljus-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "http://driveinbottleshop.dk/category/ol-cider/veteol/"));
        drinks.addAll(scrapeDrinks("Alkoläsk och Cider", "", "http://driveinbottleshop.dk/category/ol-cider/alkolask-cider/"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande", "http://driveinbottleshop.dk/category/vin/mousserande-viner/"));
        return drinks;
    }

    private void getElementsByTextForHtmlParse(String s) throws IOException {
        Document doc;
        doc = Jsoup.connect(s).get();
        Elements articles = doc.getElementsByClass("product-search");

        System.out.println(articles);
    }

    private void scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Henkell Trocken 200 ml.\" href=\"http://driveinbottleshop.dk/?produkt=henkell-trocken-200-ml\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/Henkell-Trocken-86x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=henkell-trocken-200-ml\">Henkell Trocken 200 ml.</a></h3> \n" +
                "  <h4>19.95 DKK</h4> \n" +
                "  <p>Tyskland, 11,5% Alc. Halvtorrt mousserande vin En mycket lætt mousse. Doften ær lætt och har något av gul frukt øver sig. Smaken ær frisk och lætt, det fimms en liten rest av jæstsmak kvar. E... <a href=\"http://driveinbottleshop.dk/?produkt=henkell-trocken-200-ml\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65636\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Pol Clement, 20 cl\" href=\"http://driveinbottleshop.dk/?produkt=pol-clement-o2-l\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2012/01/pol_clemente_liten-106x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=pol-clement-o2-l\">Pol Clement, 20 cl</a></h3> \n" +
                "  <h4>22.95 DKK</h4> \n" +
                "  <p>Mousserande torrt vin, 20 cl, 10,5 % alk, Frankrike Druva: Ugni Blanc och Chenin Blanc Det här vinet görs främst på druvor från Loire. CFGV som gör det här vinet är väl förankrat med m... <a href=\"http://driveinbottleshop.dk/?produkt=pol-clement-o2-l\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"68203\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Asti Gancia 20 cl.\" href=\"http://driveinbottleshop.dk/?produkt=asti-gancia-4x20-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/11/asti-Gancia-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=asti-gancia-4x20-cl\">Asti Gancia 20 cl.</a></h3> \n" +
                "  <h4>24.95 DKK</h4> \n" +
                "  <p>Asti spumante, Italien, 7,5% alk. Søtt mousserande vin. En lætt och fin mousse. Doften ær lætt med en mycket trevlig muscatkaraktær. Smaken ær typisk før ett Asti-vin, mycket trevlig kara... <a href=\"http://driveinbottleshop.dk/?produkt=asti-gancia-4x20-cl\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"67549\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Martini Asti 20 cl\" href=\"http://driveinbottleshop.dk/?produkt=martini-asti-20-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/02/martini-asti-20cl-gennemsigtig-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=martini-asti-20-cl\">Martini Asti 20 cl</a></h3> \n" +
                "  <h4>24.95 DKK</h4> \n" +
                "  <p>Asti, Italien, 20cl , 7,5 % alc. <span id=\"result_box\" class=\"\" lang=\"sv\">När du häller det här läckra mousserande vinet i glasögonen kan du njuta av många bubblor som tillsammans bildar ett ... <a href=\"http://driveinbottleshop.dk/?produkt=martini-asti-20-cl\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76704\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Canti Prosecco Extra Dry 20 cl.\" href=\"http://driveinbottleshop.dk/?produkt=canti-prosecco-extra-dry-20-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/04/Canti-prosecco-20-cl.-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=canti-prosecco-extra-dry-20-cl\">Canti Prosecco Extra Dry 20 cl.</a></h3> \n" +
                "  <h4>29.95 DKK</h4> \n" +
                "  <p>Torrt mousserande vin, <strong>20 cl</strong>. 11 % alk, Italien... <a href=\"http://driveinbottleshop.dk/?produkt=canti-prosecco-extra-dry-20-cl\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"75519\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Verdi Raspberry Sparkletini\" href=\"http://driveinbottleshop.dk/?produkt=verdi-raspberry-75-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/Verdi-raspberry-117x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=verdi-raspberry-75-cl\">Verdi Raspberry Sparkletini</a></h3> \n" +
                "  <h4>39.95 DKK</h4> \n" +
                "  <p>Mousserande fruktdrink, 75 cl, 5 % alk, Italien <span id=\"result_box\" class=\"\" lang=\"sv\">Sparkletini hallon av värde är härligt färskt med en ren och lätt smak av hallon. En vacker spumante... <a href=\"http://driveinbottleshop.dk/?produkt=verdi-raspberry-75-cl\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65738\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Verdi Peach Sparkletini\" href=\"http://driveinbottleshop.dk/?produkt=verdi-peach-75-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/Verdi-peach-112x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=verdi-peach-75-cl\">Verdi Peach Sparkletini</a></h3> \n" +
                "  <h4>39.95 DKK</h4> \n" +
                "  <p>Mousserande fruktdrink, 75 cl, 5 % alk, Italien <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"\">Sparkletini Peach efter värde har en härlig smak av mogna, söta persikor. En vacker spuma... <a href=\"http://driveinbottleshop.dk/?produkt=verdi-peach-75-cl\">Se detaljer</a></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65739\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Verdi Elderflower Sparkletini\" href=\"http://driveinbottleshop.dk/?produkt=verdi-elderflower\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2013/06/elderflower_verdi-124x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=verdi-elderflower\">Verdi Elderflower Sparkletini</a></h3> \n" +
                "  <h4>39.95 DKK</h4> \n" +
                "  <p>Mousserande fruktdrink, 75 cl, 5 % alk, Italien <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Verdi fläderblom har en härlig, frisk smak av fläderblom med en trevlig välbala... <a href=\"http://driveinbottleshop.dk/?produkt=verdi-elderflower\">Se detaljer</a></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"71537\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Exquisit Jordgubbe\" href=\"http://driveinbottleshop.dk/?produkt=royal-jordgubbe\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/exquisit-101x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=royal-jordgubbe\">Exquisit Jordgubbe</a></h3> \n" +
                "  <h4>39.95 DKK</h4> \n" +
                "  <p>Mousserande fruktvin, 75 cl, 8,5 % alk, Tyskland Generös doft av&nbsp;röda bär och vilda örter. Mycket uppfriskande syra med stor koncentration, generös frukt, örtiga toner och lång eftersmak.Et... <a href=\"http://driveinbottleshop.dk/?produkt=royal-jordgubbe\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65326\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Verdi strawberry\" href=\"http://driveinbottleshop.dk/?produkt=verdi-strawberry\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/01/verdi-strawberry-85x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=verdi-strawberry\">Verdi strawberry</a></h3> \n" +
                "  <h4>39.95 DKK</h4> \n" +
                "  <p>Mousserande fruktdrink, 75 cl, 5 % alk, Italien <span id=\"result_box\" class=\"short_text\" lang=\"sv\"><span class=\"\"><span id=\"result_box\" class=\"\" lang=\"sv\">Strawberry Sparkletini har en vacker frukt... <a href=\"http://driveinbottleshop.dk/?produkt=verdi-strawberry\">Se detaljer</a></span></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76349\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>");

        Elements articles = doc.getElementsByClass("product-search");

        articles.forEach(article -> {
            System.err.println(article.getElementsByTag("p").text() + "\n" +
                    "Name: " + extractNameFromText(article) + "\n" +
                    "Price: " + extractPriceFromText(article) + "\n" +
                    "Alcohol: " + extractAlcoholFromText(article) + "\n" +
                    "Volume: " + extractVolumeFromText(article) );
        });
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
        String volume = article.getElementsByTag("p").text();
        int clCommaIndex = volume.indexOf("cl,");
        int minIndex = clCommaIndex - 6;
        if(minIndex >= 0) {
            String substring = volume.substring(minIndex, clCommaIndex);
            if(is24Pack(volume, substring, article)) {
                return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", "")) * 24;
            }
            if(substring.matches(".*([0-9],[0-9]).*")) {
                substring = substring.replaceFirst(",", ".");
            }
            return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", ""));
        } else if(minIndex == -7) {
            int clPeriodIndex = volume.indexOf("cl.");
            minIndex = clPeriodIndex - 6;
            if(minIndex >= 0) {
                String substring = volume.substring(minIndex, clPeriodIndex);
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
        return 0f;
    }

    private boolean is24Pack(String volume, String substring, Element article) {
        if(substring.contains("x")) {
            if(volume.contains("24")) {
                return true;
            }
        } else if(extractNameFromText(article).contains("24 burkar")) {
            return true;
        }
        return false;
    }

}