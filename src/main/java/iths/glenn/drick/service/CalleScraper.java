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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalleScraper implements ScraperService{

    DrinkStorage drinkStorage;
    StoreStorage storeStorage;
    StoreEntity calle;

    float currencyExchangeRate;

    public CalleScraper(DrinkStorage drinkStorage, StoreStorage storeStorage) {
        this.drinkStorage = drinkStorage;
        this.storeStorage = storeStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {

        calle = storeStorage.findById("calle")
                .orElse(new StoreEntity("calle", "EUR"));
        currencyExchangeRate = CurrencyExchangeRateService.exchangeRate(calle.getCurrency());

        //getElementsByTextForHtmlParse("https://calle.ee/?id=37&cat=27");
        //scrapeDrinksTest("Vin", "Rött vin");

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();

        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .collect(Collectors.toList());

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));

        return filteredDrinks;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "https://calle.ee/?id=37&cat=27"));
        drinks.addAll(scrapeDrinks("Vin", "Vitt vin", "https://calle.ee/?id=37&cat=28"));
        drinks.addAll(scrapeDrinks("Vin", "Rosévin", "https://calle.ee/?id=37&cat=29"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande vin", "https://calle.ee/?id=37&cat=30"));
        drinks.addAll(scrapeDrinks("Vin", "Övrigt", "https://calle.ee/?id=37&cat=32"));
        drinks.addAll(scrapeDrinks("Vin", "BIB", "https://calle.ee/?id=37&cat=33"));
        drinks.addAll(scrapeDrinks("Öl", "Öl", "https://calle.ee/?id=37&cat=4"));
        drinks.addAll(scrapeDrinks("Sprit", "Vodka", "https://calle.ee/?id=37&cat=13"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "https://calle.ee/?id=37&cat=14"));
        drinks.addAll(scrapeDrinks("Sprit", "Cognac", "https://calle.ee/?id=37&cat=15"));
        drinks.addAll(scrapeDrinks("Sprit", "Brandy", "https://calle.ee/?id=37&cat=16"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "https://calle.ee/?id=37&cat=17"));
        drinks.addAll(scrapeDrinks("Sprit", "Bitter", "https://calle.ee/?id=37&cat=18"));
        drinks.addAll(scrapeDrinks("Sprit", "Gin", "https://calle.ee/?id=37&cat=19"));
        drinks.addAll(scrapeDrinks("Sprit", "Snaps", "https://calle.ee/?id=37&cat=20"));
        drinks.addAll(scrapeDrinks("Sprit", "Calvados", "https://calle.ee/?id=37&cat=22"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "https://calle.ee/?id=37&cat=23"));
        drinks.addAll(scrapeDrinks("Sprit", "Tequila", "https://calle.ee/?id=37&cat=24"));
        drinks.addAll(scrapeDrinks("Sprit", "Annan starksprit", "https://calle.ee/?id=37&cat=26"));
        drinks.addAll(scrapeDrinks("Sprit", "Vermouth", "https://calle.ee/?id=37&cat=25"));
        drinks.addAll(scrapeDrinks("Sprit", "Portvin", "https://calle.ee/?id=37&cat=31"));
        drinks.addAll(scrapeDrinks("Sprit", "Glögg", "https://calle.ee/?id=37&cat=40"));
        drinks.addAll(scrapeDrinks("Cider och alkoläsk", "Cider", "https://calle.ee/?id=37&cat=5"));
        drinks.addAll(scrapeDrinks("Cider och alkoläsk", "Alkoläsk", "https://calle.ee/?id=37&cat=6"));

        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements articles = doc.getElementsByClass("list-item");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> drinks.add(makeDrink(article, type, subtype)));

        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6482\"> <img src=\"https://calle.ee/product_images/9311220005517.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6482\">19 Crimes Cabernet Sauvignon 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Australia');\">Australia</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 9311220005517</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">9.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6482, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6482\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6482\" onkeyup=\"chgCartAmount(6482, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6482, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6482\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6484\"> <img src=\"https://calle.ee/product_images/9311220004343.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6484\">19 Crimes Red 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Australia');\">Australia</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 9311220004343</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">9.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6484, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6484\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6484\" onkeyup=\"chgCartAmount(6484, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6484, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6484\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5378\"> <img src=\"https://calle.ee/product_images/5704634289149.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5378\">AU Negroamaro Primitivo 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 5704634289149</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">4.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5378, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5378\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5378\" onkeyup=\"chgCartAmount(5378, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5378, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5378\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5990\"> <img src=\"https://calle.ee/product_images/7804320150611.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5990\">Adobe Reserva Cabernet Sauvignon Organic 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Chile');\">Chile</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 7804320150611</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5990, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5990\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5990\" onkeyup=\"chgCartAmount(5990, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5990, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5990\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6089\"> <img src=\"https://calle.ee/product_images/7804320198521.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6089\">Adobe Reserva Syrah Organic 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Chile');\">Chile</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 7804320198521</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6089, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6089\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6089\" onkeyup=\"chgCartAmount(6089, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6089, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6089\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6162\"> <img src=\"https://calle.ee/product_images/4867601702766.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6162\">Alazani Red Semi-Sweet 75cl 11,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Georgia');\">Georgia</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 4867601702766</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.29</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6162, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6162\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6162\" onkeyup=\"chgCartAmount(6162, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6162, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6162\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=2981\"> <img src=\"https://calle.ee/product_images/3452130038199.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=2981\">Alexis Lichine Bordeaux 75cl 12%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('France');\">France</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 3452130038199</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">8.79</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(2981, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_2981\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_2981\" onkeyup=\"chgCartAmount(2981, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(2981, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_2981\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5960\"> <img src=\"https://calle.ee/product_images/28032610310107.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5960\">Allegro Primitivo Organic Red 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 28032610310107</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">7.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5960, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5960\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5960\" onkeyup=\"chgCartAmount(5960, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5960, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5960\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=1060\"> <img src=\"https://calle.ee/product_images/8016963000010.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=1060\">Amarone della Valpolicella 75cl 15%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8016963000010</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">16.69</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(1060, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_1060\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_1060\" onkeyup=\"chgCartAmount(1060, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(1060, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_1060\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5909\"> <img src=\"https://calle.ee/product_images/8023415000382.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5909\">Amore Assoluto Biologico 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8023415000382</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5909, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5909\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5909\" onkeyup=\"chgCartAmount(5909, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5909, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5909\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5390\"> <img src=\"https://calle.ee/product_images/8023415060027.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5390\">Antiche Terre Amarone Valpolicella DOCG 75cl 15%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8023415060027</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">17.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5390, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5390\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5390\" onkeyup=\"chgCartAmount(5390, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5390, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5390\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5391\"> <img src=\"https://calle.ee/product_images/8023415060072.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5391\">Antiche Terre Solo Passione Rosso 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8023415060072</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">5.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5391, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5391\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5391\" onkeyup=\"chgCartAmount(5391, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5391, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5391\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5386\"> <img src=\"https://calle.ee/product_images/8023415060034.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5386\">Antiche Terre Valpolicella Ripasso Superiore 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8023415060034</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">8.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5386, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5386\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5386\" onkeyup=\"chgCartAmount(5386, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5386, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5386\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6792\"> <img src=\"https://calle.ee/product_images/5704634186660.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6792\">Archidamus III Primitivo di Manduria 75cl 15%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 5704634186660</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">10.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6792, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6792\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6792\" onkeyup=\"chgCartAmount(6792, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6792, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6792\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5993\"> <img src=\"https://calle.ee/product_images/8003295011531.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5993\">Ascarone Rosso IGT Puglia 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8003295011531</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">4.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5993, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5993\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5993\" onkeyup=\"chgCartAmount(5993, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5993, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5993\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6510\"> <img src=\"https://calle.ee/product_images/8033765186612.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6510\">Asio Otus Cab. Merlot Shiraz 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8033765186612</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">7.39</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6510, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6510\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6510\" onkeyup=\"chgCartAmount(6510, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6510, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6510\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5375\"> <img src=\"https://calle.ee/product_images/5704634283796.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5375\">Aussiemore McLaren Shiraz 75cl 14,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Australia');\">Australia</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 5704634283796</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">5.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5375, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5375\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5375\" onkeyup=\"chgCartAmount(5375, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5375, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5375\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6130\"> <img src=\"https://calle.ee/product_images/9311043083099.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6130\">Banrock Station Shiraz-Mataro 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Australia');\">Australia</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 9311043083099</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">5.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6130, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6130\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6130\" onkeyup=\"chgCartAmount(6130, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6130, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6130\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5225\"> <img src=\"https://calle.ee/product_images/8032793970262.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5225\">Barbaresco 2014 DOCG 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8032793970262</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">18.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5225, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5225\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5225\" onkeyup=\"chgCartAmount(5225, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5225, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5225\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5224\"> <img src=\"https://calle.ee/product_images/8032793950066.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5224\">Barbera d´Alba Egidio 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8032793950066</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">11.59</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5224, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5224\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5224\" onkeyup=\"chgCartAmount(5224, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5224, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5224\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5588\"> <img src=\"https://calle.ee/product_images/018341751109.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5588\">Barefoot Merlot 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('USA');\">USA</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 018341751109</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5588, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5588\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5588\" onkeyup=\"chgCartAmount(5588, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5588, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5588\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5226\"> <img src=\"https://calle.ee/product_images/8032793950073.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5226\">Barolo 2015 DOCG 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8032793950073</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">23.39</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5226, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5226\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5226\" onkeyup=\"chgCartAmount(5226, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5226, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5226\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6317\"> <img src=\"https://calle.ee/product_images/8030423003212.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6317\">Barone Montalto Passivento Rosso 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8030423003212</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">7.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6317, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6317\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6317\" onkeyup=\"chgCartAmount(6317, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6317, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6317\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6416\"> <img src=\"https://calle.ee/product_images/7790093000300.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6416\">Bodegas Etchart Arnaldo B 75cl 14,9%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Argentiina');\">Argentiina</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 7790093000300</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">10.59</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6416, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6416\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6416\" onkeyup=\"chgCartAmount(6416, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6416, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6416\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6176\"> <img src=\"https://calle.ee/product_images/8008863013726.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6176\">Botter Merlot 75cl 12%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8008863013726</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">5.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6176, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6176\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6176\" onkeyup=\"chgCartAmount(6176, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6176, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6176\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6174\"> <img src=\"https://calle.ee/product_images/8008863055887.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6174\">Botter Primitivo Salento 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Italy');\">Italy</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 8008863055887</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">5.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6174, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6174\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6174\" onkeyup=\"chgCartAmount(6174, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6174, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6174\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5470\"> <img src=\"https://calle.ee/product_images/9414024279150.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5470\">Brancott Pinot Noir 75cl 13,5%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('New Zealand');\">New Zealand</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 9414024279150</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">9.59</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5470, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5470\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5470\" onkeyup=\"chgCartAmount(5470, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5470, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5470\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6354\"> <img src=\"https://calle.ee/product_images/7350005880484.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6354\">Buffalo Zinfandel Bourbon Barrel 75cl 14%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('USA');\">USA</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 7350005880484</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">7.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6354, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6354\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6354\" onkeyup=\"chgCartAmount(6354, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6354, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6354\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=6789\"> <img src=\"https://calle.ee/product_images/5727510520679.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=6789\">Butcher´s Cut Malbec 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Argentina');\">Argentina</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 5727510520679</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">6.99</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6789, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_6789\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_6789\" onkeyup=\"chgCartAmount(6789, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(6789, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_6789\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>\n" +
                "<div class=\"list-item\"> \n" +
                " <!-- start of item --> \n" +
                " <div class=\"list-item-image\"> \n" +
                "  <a href=\"https://calle.ee/./?id=37&amp;prod=5996\"> <img src=\"https://calle.ee/product_images/5604575110024.jpg\" alt=\"\"> </a> \n" +
                " </div> \n" +
                " <div class=\"list-item-text\"> \n" +
                "  <h1><a href=\"https://calle.ee/./?id=37&amp;prod=5996\">Cabriz Red DOC 75cl 13%</a></h1> \n" +
                "  <p><a href=\"#\" onclick=\"filterProdCountry('Portugal');\">Portugal</a></p> \n" +
                " </div> \n" +
                " <div class=\"list-item-text-additional\"> \n" +
                "  <p>Produktkoden: 5604575110024</p> \n" +
                " </div> \n" +
                " <div class=\"list-item-cart\">\n" +
                "  <p> <span class=\"hind\">8.49</span> <span class=\"valuuta\">€</span></p> \n" +
                "  <div class=\"list-item-cart-l\"> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5996, -1); return false;\">-</a> \n" +
                "   <form id=\"prform_5996\" method=\"post\" action=\"\"> \n" +
                "    <div> \n" +
                "     <input type=\"text\" name=\"item-num\" class=\"item-num\" id=\"product_5996\" onkeyup=\"chgCartAmount(5996, this.value);return false;\" value=\"\"> \n" +
                "    </div> \n" +
                "   </form> \n" +
                "   <a href=\"#\" onclick=\"chgCartAmount(5996, +1); return false;\">+</a> \n" +
                "   <div class=\"clear\"></div> \n" +
                "  </div> \n" +
                "  <p class=\"lisa\"> <span id=\"prodTXT_5996\">Lägg till i varukorgen</span></p> \n" +
                " </div> \n" +
                " <div class=\"clear\"></div> \n" +
                "</div>");

        Elements articles = doc.getElementsByClass("list-item");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();
        articles.forEach(article -> {
            System.err.println(article.getElementsByTag("h1").text() + "\n" +
                    "Name: " + extractNameFromText(article) + "\n" +
                    "Price: " + extractPriceFromText(article) + "\n" +
                    "Alcohol: " + extractAlcoholFromText(article) + "\n" +
                    "Volume: " + extractVolumeFromText(article) );
             drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;
    }

    private void getElementsByTextForHtmlParse(String s) throws IOException {
        Document doc;
        doc = Jsoup.connect(s).get();
        Elements articles = doc.getElementsByClass("list-item");

        System.out.println(articles);
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        System.err.println(article.getElementsByTag("h1").text());
        String name = extractNameFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article);
        float price = extractPriceFromText(article) * currencyExchangeRate;

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, calle);
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByTag("h1").text();
        int lIndex = volumeString.lastIndexOf('l');
        char volumePrefix = volumeString.charAt(lIndex - 1);
        if(!String.valueOf(volumePrefix).matches("[cm ]")) {  //If lIndex is not the last l in volumeString the volume
            lIndex = volumeString.substring(lIndex).lastIndexOf('l'); // Second from last 'l'
        }
        int spaceBeforeVolumeIndex = volumeString.substring(0, lIndex).lastIndexOf(' ');
        int multiPackMultiplier = extractMultiPackMultiplier(volumeString.substring(spaceBeforeVolumeIndex, lIndex));
        int volumeMultiplier = volumePrefixMultiplier(volumeString, volumePrefix) * multiPackMultiplier;
        volumeString = volumeString.substring(spaceBeforeVolumeIndex, lIndex)
                .replaceAll("[a-öA-Ö %]", "")
                .replace(",", ".");
        return Float.parseFloat(volumeString) * volumeMultiplier;

    }

    private int extractMultiPackMultiplier(String volumeString) {
        int packMultiplier = 1;
        if(volumeString.contains("x")) {
            String multiPackString = volumeString.substring(0, volumeString.indexOf('x')).trim();
            packMultiplier = Integer.parseInt(multiPackString);
        }
        return packMultiplier;
    }

    private int volumePrefixMultiplier(String volumeString, char volumePrefix) {
        if(volumePrefix == 'c') {
            return 10;
        } else if(volumePrefix == 'm') {
            return 1;
        } else if(String.valueOf(volumePrefix).matches("[0-9]")) {
            System.out.println("In litres? " + volumeString);
            return 1000;
        }
        System.err.println(volumeString + ": " + volumePrefix + ": No volume prefix?");
        return 1;
    }

    private float extractAlcoholFromText(Element article) {
        String alcoholString = article.getElementsByTag("h1").text();
        int percIndex = alcoholString.indexOf('%');
        int lIndex = alcoholString.lastIndexOf('l');
        if(isUsualDescription(percIndex, lIndex)) {
            String alcoholSubString = alcoholString.substring(lIndex + 1, percIndex).trim();
            alcoholSubString = alcoholSubString.replace(",", ".").replaceAll("[a-zA-Z ]", "");
            return Float.parseFloat(alcoholSubString);
        } else {
            return extractAlcoholFromOddCaseText(percIndex, lIndex, alcoholString);
        }
    }

    private float extractAlcoholFromOddCaseText(int percIndex, int lIndex, String alcoholString) {
        if(alcoholString.contains("klaasi")) {
            lIndex = alcoholString.substring(0, alcoholString.indexOf('+')).lastIndexOf('l');
            String alcoholSubString = alcoholString.substring(lIndex + 1, percIndex).trim();
            alcoholSubString = alcoholSubString.replace(",", ".").replaceAll("[a-zA-Z ]", "");
            return Float.parseFloat(alcoholSubString);
        }
        return 0;
    }

    private boolean isUsualDescription(int percIndex, int lIndex) {
        return (percIndex > 0 && lIndex > 0 && percIndex > lIndex);
    }

    private String extractNameFromText(Element article) {
        String nameString = article.getElementsByTag("h1").text();
        int lIndex = nameString.lastIndexOf('l');
        int spaceBeforeVolume = nameString.substring(0, lIndex).lastIndexOf(' ');
        return nameString.substring(0, spaceBeforeVolume);
    }

    private float extractPriceFromText(Element article) {
        String priceString = article.getElementsByClass("hind").text();
        return Float.parseFloat(priceString);
    }


}
