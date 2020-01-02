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

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Öl", "Mörk öl", "http://driveinbottleshop.dk/category/ol-cider/mork-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Ljus öl", "http://driveinbottleshop.dk/category/ol-cider/ljus-ol/"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "http://driveinbottleshop.dk/category/ol-cider/veteol/"));
        drinks.addAll(scrapeDrinks("Alkoläsk och Cider", "", "http://driveinbottleshop.dk/category/ol-cider/alkolask-cider/"));

        //scrapeDrinksTest("Alkoläsk och cider", "Cider");
        //getElementsByTextForHtmlParse("http://driveinbottleshop.dk/category/ol-cider/alkolask-cider/");
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
                " <a class=\"thumbnail\" title=\"Breezer Strawberry\" href=\"http://driveinbottleshop.dk/?produkt=breezer-strawberry\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/10/breezer_strawberry-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-strawberry\">Breezer Strawberry</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4 % alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Stor doftande doft med tydlig karaktär av jordgubbar och inslag av blodapelsin.</span>Se detaljer</span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"75971\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Breezer Mango\" href=\"http://driveinbottleshop.dk/?produkt=breezer-mango\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/01/Breezer-mango-gennemsigtig-48x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-mango\">Breezer Mango</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4 % alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Bacardi Breezer mango har en stor fruktig doft med karaktär av mango och lite orange.</span>... <a href=\"http://driveinbottleshop.dk/?produkt=breezer-mango\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76559\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Breezer Pineapple\" href=\"http://driveinbottleshop.dk/?produkt=breezer-ananas\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/breezer_pineapple-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-ananas\">Breezer Pineapple</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4 % alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Bacardi Breezer Ananas är en ananas versionen av den populära alkoläsk.</span> Den har en ... <a href=\"http://driveinbottleshop.dk/?produkt=breezer-ananas\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65101\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Breezer Lime\" href=\"http://driveinbottleshop.dk/?produkt=breezer-lime\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/breezer_lime-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-lime\">Breezer Lime</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4 % alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Bacardi Breezer Lime har en uppfriskande doft av lime och har en lätt syrligt smak av lime\n" +
                "     <!--... <a href=\"http://driveinbottleshop.dk/?produkt=breezer-lime\"-->Se detaljer</span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65207\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Breezer Watermelon\" href=\"http://driveinbottleshop.dk/?produkt=breezer-watermelon\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2011/03/breezer_watermelon-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-watermelon\">Breezer Watermelon</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4% alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"\">Bacardi breezer watermelon har en doft av melon, apelsin och papaya.</span> <span class=\"alt-edited\">De... <a href=\"http://driveinbottleshop.dk/?produkt=breezer-watermelon\">Se detaljer</a></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65843\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Breezer Orange\" href=\"http://driveinbottleshop.dk/?produkt=breezer-tropical-orange\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2012/07/breezer_orange-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=breezer-tropical-orange\">Breezer Orange</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 cl, 4 % alk, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\">Bacardi Brezzer Orango har en stor fruktig smak med karaktäristisk natur av apelsin, element i citron.</span> \n" +
                "   <spa... <a href=\"http://driveinbottleshop.dk/?produkt=breezer-tropical-orange\">\n" +
                "    Se detaljer\n" +
                "   </spa...></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"69459\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Smirnoff Ice 27,5cl\" href=\"http://driveinbottleshop.dk/?produkt=smirnoff-ice\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2013/02/smirnoff_ice-110x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=smirnoff-ice\">Smirnoff Ice 27,5cl</a></h3> \n" +
                "  <h4>12.95 DKK</h4> \n" +
                "  <p>Alkoläsk, 27,5 &nbsp;cl, 4 % Alc. <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">En vodka blandad dryck med den klassiska smaken av citron</span></span> <span id=\"result_box\" cla... <a href=\"http://driveinbottleshop.dk/?produkt=smirnoff-ice\">Se detaljer</span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"65404\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Somersby Apple 24 Burkar\" href=\"http://driveinbottleshop.dk/?produkt=somersby-apple-24-burkar\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2013/05/somersby_apple-150x116.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=somersby-apple-24-burkar\">Somersby Apple 24 Burkar</a></h3> \n" +
                "  <h4>189.95 DKK</h4> \n" +
                "  <p>Äpplecider, 24 x 33 cl, 4,5 % alk Tillgången på de olika smakerna på cider kan variera. Om du vill vara säker på att en viss smak finns i butiken ring oss gärna och fråga. Pant tillkomme... <a href=\"http://driveinbottleshop.dk/?produkt=somersby-apple-24-burkar\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"71276\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Somersby Elderflower/Lime 24 burkar\" href=\"http://driveinbottleshop.dk/?produkt=somersby-elderflowerlime-24-burkar\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2015/03/Somersby-elderflower-lime-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=somersby-elderflowerlime-24-burkar\">Somersby Elderflower/Lime 24 burkar</a></h3> \n" +
                "  <h4>189.95 DKK</h4> \n" +
                "  <p>Cider med &nbsp;fläderbloms och limesmak, 24 x 33 cl, 4,5 % alk Tillgången på de olika smakerna på cider kan variera. Om du vill vara säker på att en viss smak finns i butiken ring oss gärna och... <a href=\"http://driveinbottleshop.dk/?produkt=somersby-elderflowerlime-24-burkar\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"73538\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Briska Päroncider 24 burkar\" href=\"http://driveinbottleshop.dk/?produkt=briska-paroncider-24-burkar\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/02/Briska-paeron-cider-gennemsigtig-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=briska-paroncider-24-burkar\">Briska Päroncider 24 burkar</a></h3> \n" +
                "  <h4>189.95 DKK</h4> \n" +
                "  <p>Cider, Sverige, 33cl, 4,5% alk. Mycket fruktig, söt smak med tydlig karaktär av päron, inslag av gröna äpplen och vingummi. Serveras vid 6-8°C som sällskapsdryck. <b>Pris på Systemet for... <a href=\"http://driveinbottleshop.dk/?produkt=briska-paroncider-24-burkar\">Se detaljer</a></b></p>\n" +
                "  <b> \n" +
                "   <form class=\"add-to-cart\"> \n" +
                "    <label for=\"qty\">Kvantitet</label> \n" +
                "    <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "    <input type=\"hidden\" name=\"product_id\" value=\"76720\"> \n" +
                "    <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "    <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "   </form> </b>\n" +
                " </section>\n" +
                " <b> </b>\n" +
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
            is24Pack(volume, substring, article);
            if(substring.contains("x") || extractNameFromText(article).contains("24 burkar")) {
                if(volume.contains("24")) {
                    return Float.parseFloat(substring.replaceAll("[a-öA-Ö, ]", "")) * 24;
                }
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