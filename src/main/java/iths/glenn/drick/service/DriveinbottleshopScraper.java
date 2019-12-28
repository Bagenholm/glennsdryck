package iths.glenn.drick.service;

import iths.glenn.drick.entity.DrinkEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DriveinbottleshopScraper implements ScraperService {
    @Override
    public List<DrinkEntity> scrape() throws IOException {
     /*   ArrayList<DrinkEntity> drinks = new ArrayList<>();

        Document lightBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/ljus-ol/").get();
//        Document wheatBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/veteol/").get();
//        Document darkBeer = Jsoup.connect("http://driveinbottleshop.dk/category/ol-cider/mork-ol/").get();

        Elements articles = lightBeer.getElementsByClass("product-search");

        articles.forEach(article -> drinks.add(makeDrink(article)));



        //drinks.forEach(System.out::println);
        return drinks; */

        Document doc;
        doc = Jsoup.parse("<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Urquell Pilsner\" href=\"http://driveinbottleshop.dk/?produkt=urquell-pilsner\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2019/05/Urquell-pilsner-ny-d--se-Gennemsigtig-87x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=urquell-pilsner\">Urquell Pilsner</a></h3> \n" +
                "  <h4>10.95 DKK</h4> \n" +
                "  <p>Øl, ljus,Tjeckien,&nbsp;Plzen , 57cl, 4,4%alk. Pilsner Urquell är den ursprungliga lager från Plzen i Tjeckien! Bryggeriet Plzenský Prazdroj revolutionerade öl bryggning år 1842 genom att göra v... <a href=\"http://driveinbottleshop.dk/?produkt=urquell-pilsner\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"77479\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"The Tail Of A Whale\" href=\"http://driveinbottleshop.dk/?produkt=the-tail-of-a-whale\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/11/Tail-of-a-whale-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=the-tail-of-a-whale\">The Tail Of A Whale</a></h3> \n" +
                "  <h4>10.95 DKK</h4> \n" +
                "  <p>Ljus veteöl, 33 cl, 4,8 % alk, Brutal Brewing, Sverige En frisk humleblommig doft med tydliga inslag av citrus och grapefrukt. Smaken är humlearomatisk med en lätt balanserad maltighet med en l... <a href=\"http://driveinbottleshop.dk/?produkt=the-tail-of-a-whale\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76190\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"A Ship Full Of IPA\" href=\"http://driveinbottleshop.dk/?produkt=a-ship-full-of-ipa\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/11/A-ship-full-of-IPA-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=a-ship-full-of-ipa\">A Ship Full Of IPA</a></h3> \n" +
                "  <h4>10.95 DKK</h4> \n" +
                "  <p>Ljus öl, 33cl, 5,8% Alk, Brutal brewing, Sverige Kraftig humlearomatisk med inslag av grape, melon och citrus. Maltig med en kraftig markerad humlebeska och angenäm humlearom med inslag av grape... <a href=\"http://driveinbottleshop.dk/?produkt=a-ship-full-of-ipa\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76191\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Lucky Buddha\" href=\"http://driveinbottleshop.dk/?produkt=lucky-buddha\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2015/10/lucky_buddha-133x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=lucky-buddha\">Lucky Buddha</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Ljus öl, 33 cl, 4,7 % alk, Kina <span id=\"result_box\" class=\"\" lang=\"sv\">Lucky Buddha är ett äkta asiatiskt lager med mycket smak och en ren, ren finish. Lucky Buddha är 100% naturlig, ren och ... <a href=\"http://driveinbottleshop.dk/?produkt=lucky-buddha\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"74213\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Clausthaler Premium Alkoholfrei 50 cl.\" href=\"http://driveinbottleshop.dk/?produkt=clausthaler-premium-alkoholfrei-50-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2017/08/Clausthaler-alkoholfri-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=clausthaler-premium-alkoholfrei-50-cl\">Clausthaler Premium Alkoholfrei 50 cl.</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Ljus öl, 50 cl, 0,45 % alkohol, Bayern, Tyskland En ljus frisk helmaltsöl som passar vid alla de tillfällen då det är gott med en kall öl. Clausthaler är bryggd enligt den bayerska renhetsla... <a href=\"http://driveinbottleshop.dk/?produkt=clausthaler-premium-alkoholfrei-50-cl\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"75802\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"San Miguel Especial 50 cl.\" href=\"http://driveinbottleshop.dk/?produkt=san-miguel-especial-50-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2015/06/San-Miguel-Da--se-50cl-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=san-miguel-especial-50-cl\">San Miguel Especial 50 cl.</a></h3> \n" +
                "  <h4>11.95 DKK</h4> \n" +
                "  <p>Ljus öl, 50 cl, 5,4 % alk, Spanien IBU: 25 Ljus mild öl från Spanien. En lageröl med rund mild smak och en fin skumkrona. Ganska liten beska med bra sädeskaraktär och en aning citrus. En k... <a href=\"http://driveinbottleshop.dk/?produkt=san-miguel-especial-50-cl\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"73768\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Flensburger Gold 33 cl\" href=\"http://driveinbottleshop.dk/?produkt=flensburger-gold-33-cl\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2012/03/flensburger-gold-107x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=flensburger-gold-33-cl\">Flensburger Gold 33 cl</a></h3> \n" +
                "  <h4>12.95 DKK</h4> \n" +
                "  <p>Ljust öl, 33 cl, Tyskland <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"\">Brewed med lätt karamellmalt och bara de finaste doftpoporna.</span> <span class=\"\">En mycket lätt, tunn öl med... <a href=\"http://driveinbottleshop.dk/?produkt=flensburger-gold-33-cl\">Se detaljer</a></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"68910\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Corona Extra\" href=\"http://driveinbottleshop.dk/?produkt=corona-ekstra\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2013/08/corona_extra-126x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=corona-ekstra\">Corona Extra</a></h3> \n" +
                "  <h4>12.95 DKK</h4> \n" +
                "  <p>Ljus öl, 33 cl, Mexiko, 4,5% alk. <span id=\"result_box\" class=\"\" lang=\"sv\">Corona Extra är en importerad mexikansk pilsner öl. Den gyllene ölen är livlig och ger en skarp vit skumkrona. Doften... <a href=\"http://driveinbottleshop.dk/?produkt=corona-ekstra\">Se detaljer</a></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"71726\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Paulaner burk, Hefeweizen, Bayern\" href=\"http://driveinbottleshop.dk/?produkt=paulaner-burk\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/01/Paulaner-hefe-gennemsigtig-78x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=paulaner-burk\">Paulaner burk, Hefeweizen, Bayern</a></h3> \n" +
                "  <h4>12.95 DKK</h4> \n" +
                "  <p>Tyskland, veteöl, ljus, 33cl, 5,6% alk. Tysk ljus veteöl med sin naturliga jästfällning. Fruktig mild smak med en lätt citruston. Paulaner startades av de fattiga munkarna på ett kloster i... <a href=\"http://driveinbottleshop.dk/?produkt=paulaner-burk\">Se detaljer</a></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76455\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n" +
                "<article class=\"product-search\"> \n" +
                " <a class=\"thumbnail\" title=\"Flensburger Pilsner\" href=\"http://driveinbottleshop.dk/?produkt=flensburger-pilsner\" style=\"background-image:url(http://driveinbottleshop.dk/wp-content/uploads/2018/01/flensburger-pilsener-med-glas-gennemsigtig-130x130.png)\"></a> \n" +
                " <section class=\"info\"> \n" +
                "  <h3><a href=\"http://driveinbottleshop.dk/?produkt=flensburger-pilsner\">Flensburger Pilsner</a></h3> \n" +
                "  <h4>12.95 DKK</h4> \n" +
                "  <p>Mörk Öl, 33 cl, Tyskland, 4,8% alk. <span id=\"result_box\" class=\"\" lang=\"sv\"><span class=\"alt-edited\">Dess ovanligt skarp kryddig doft och användningen av utvalda sorter av humle står för god ... <a href=\"http://driveinbottleshop.dk/?produkt=flensburger-pilsner\">Se detaljer</a></span></span></p> \n" +
                "  <form class=\"add-to-cart\"> \n" +
                "   <label for=\"qty\">Kvantitet</label> \n" +
                "   <input type=\"text\" class=\"text\" name=\"qty\" value=\"1\"> \n" +
                "   <input type=\"hidden\" name=\"product_id\" value=\"76501\"> \n" +
                "   <input type=\"hidden\" name=\"ajaxurl\" value=\"http://driveinbottleshop.dk/wp-admin/admin-ajax.php\"> \n" +
                "   <input type=\"submit\" class=\"submit\" name=\"submit_to_cart\" value=\"Lägg i shoppinglista\"> \n" +
                "  </form> \n" +
                " </section> \n" +
                "</article>\n");

        Elements articles = doc.getElementsByClass("product-search");

        articles.forEach(System.out::println);



        //System.err.println(articles.get(0).getElementsByTag("p").text());

        articles.forEach(article -> {
            System.err.println(article.getElementsByTag("p").text());
            String alkoholhalt = article.getElementsByTag("p").text();
            int percIndex = alkoholhalt.indexOf("%");
            int minIndex = percIndex-6;
            if(minIndex >= 0) {
                String substring = alkoholhalt.substring(minIndex, percIndex + 1);
                System.err.println(substring);

            }
        });
        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        //drinks.forEach(System.out::println);
        return Collections.emptyList();
    }

    private DrinkEntity makeDrink(Element article) {
        String name = article.getElementsByTag("h3").text();

        return new DrinkEntity("", "", "", 44.4f, 44.4f);
    }

}