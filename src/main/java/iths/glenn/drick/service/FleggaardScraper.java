package iths.glenn.drick.service;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FleggaardScraper implements ScraperService {

    DrinkStorage drinkStorage;
    StoreStorage storeStorage;
    StoreEntity fleggaard;

    BrowserEngine browser;
    float currencyExchangeRate;

    public FleggaardScraper(DrinkStorage drinkStorage, StoreStorage storeStorage) {
        this.drinkStorage = drinkStorage;
        this.storeStorage = storeStorage;
    }

    @Override
    public List<DrinkEntity> scrape() throws IOException {

        fleggaard = storeStorage.findById("fleggaard")
                .orElse(new StoreEntity("fleggaard", "DKK"));
        currencyExchangeRate = CurrencyExchangeRateService.exchangeRate(fleggaard.getCurrency());

        ArrayList<DrinkEntity> drinks = scrapeAllDrinks();

        ArrayList<DrinkEntity> filteredDrinks = (ArrayList<DrinkEntity>) drinks.stream()
                .filter(drinkEntity -> drinkEntity.getAlcoholPerPrice() != 0)
                .filter(drinkEntity -> !drinkEntity.getName().trim().isEmpty())
                .collect(Collectors.toList());

        fleggaard.setDrinks(filteredDrinks);

        filteredDrinks.forEach(drinkEntity -> drinkStorage.save(drinkEntity));

        return filteredDrinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinks(String type, String subtype, String url) throws IOException {
        browser = BrowserFactory.getWebKit(); // Throws NPE if run on JDK11 without JavaFX added as a library
        Page page = browser.navigate(url);
        String htmlString = page.getDocument().queryAll(".products").toString();
        Document doc = Jsoup.parse(htmlString);
        Elements articles = doc.getElementsByClass("product");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        articles.forEach(article -> {
            drinks.add(makeDrink(article, type, subtype));
        });
        return drinks;
    }

    private ArrayList<DrinkEntity> scrapeDrinksTest(String type, String subtype) {
        Document doc = Jsoup.parse("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  [WebKitElement [element=], WebKitElement [element=\n" +
                "  <div class=\"product highlight\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Newcastle-Brown-Ale-4-7%25-24x0-33-l-_1215096_40381.aspx\" title=\"Newcastle Brown Ale 4,7% 24x0,33 l.\" class=\"productName\">Newcastle Brown Ale 4,7% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Newcastle-Brown-Ale-4-7%25-24x0-33-l-_1215096_40381.aspx\" title=\"Newcastle Brown Ale 4,7% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1559974&amp;sizeId=4317\" title=\"Newcastle Brown Ale 4,7% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1559974&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 99,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>99\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 13,42\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 12,63\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"1215096\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(1215096, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product highlight\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Royal-Brown-Ale-4-6%25-24x0-33-l-_1210980_40381.aspx\" title=\"Royal Brown Ale 4,6% 24x0,33 l.\" class=\"productName\">Royal Brown Ale 4,6% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Royal-Brown-Ale-4-6%25-24x0-33-l-_1210980_40381.aspx\" title=\"Royal Brown Ale 4,6% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1334172&amp;sizeId=4317\" title=\"Royal Brown Ale 4,6% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1334172&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 59,99\n" +
                "    </div>\n" +
                "    <div class=\"familyPriceAmount\">\n" +
                "     Ta' 3 for\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>159\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 21,48\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. v/merkøb DKK 6,73\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"1210980\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(1210980, 3,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product highlight\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Leffe-Brune-6-5%25-6x0-75-l-_6299370_40381.aspx\" title=\"Leffe Brune 6,5% 6x0,75 l.\" class=\"productName\">Leffe Brune 6,5% 6x0,75 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Leffe-Brune-6-5%25-6x0-75-l-_6299370_40381.aspx\" title=\"Leffe Brune 6,5% 6x0,75 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433295&amp;sizeId=4317\" title=\"Leffe Brune 6,5% 6x0,75 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433295&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 129,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>129\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 17,45\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 28,89\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"6299370\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(6299370, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Grimbergen-Double-6-5%25-24x0-33-l-_2101473_40381.aspx\" title=\"Grimbergen Double 6,5% 24x0,33 l.\" class=\"productName\">Grimbergen Double 6,5% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Grimbergen-Double-6-5%25-24x0-33-l-_2101473_40381.aspx\" title=\"Grimbergen Double 6,5% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1406077&amp;sizeId=4317\" title=\"Grimbergen Double 6,5% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1406077&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 169,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>169\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 22,82\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 21,46\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"2101473\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(2101473, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Ale-No-16-5-7%25-24x0-33-l-_1213190_40381.aspx\" title=\"Ale No. 16 5,7% 24x0,33 l.\" class=\"productName\">Ale No. 16 5,7% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Ale-No-16-5-7%25-24x0-33-l-_1213190_40381.aspx\" title=\"Ale No. 16 5,7% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=121094&amp;sizeId=4317\" title=\"Ale No. 16 5,7% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=121094&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <img src=\"/media/181/img_splash/FleggaardAvisDK.png?5\" class=\"splashImg2 splashImg-BottomRight\">\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 79,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>79\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 10,74\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 10,10\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"1213190\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(1213190, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Grimbergen-Blonde-6-7%25-24x0-33-l-_2101472_40381.aspx\" title=\"Grimbergen Blonde 6,7% 24x0,33 l.\" class=\"productName\">Grimbergen Blonde 6,7% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Grimbergen-Blonde-6-7%25-24x0-33-l-_2101472_40381.aspx\" title=\"Grimbergen Blonde 6,7% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1406080&amp;sizeId=4317\" title=\"Grimbergen Blonde 6,7% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1406080&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 169,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>169\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 22,82\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 21,46\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"2101472\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(2101472, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Brewmasters-India-Pale-Ale-5-2%25-24x0-33-l-_2370423_40381.aspx\" title=\"Brewmasters India Pale Ale 5,2% 24x0,33 l.\" class=\"productName\">Brewmasters India Pale Ale 5,2% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Brewmasters-India-Pale-Ale-5-2%25-24x0-33-l-_2370423_40381.aspx\" title=\"Brewmasters India Pale Ale 5,2% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=627451&amp;sizeId=4317\" title=\"Brewmasters India Pale Ale 5,2% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=627451&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 99,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>99\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 13,42\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 12,63\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"2370423\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(2370423, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Leffe-Brune-6-5%25-24x0-33-l-_6299369_40381.aspx\" title=\"Leffe Brune 6,5% 24x0,33 l.\" class=\"productName\">Leffe Brune 6,5% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Leffe-Brune-6-5%25-24x0-33-l-_6299369_40381.aspx\" title=\"Leffe Brune 6,5% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433294&amp;sizeId=4317\" title=\"Leffe Brune 6,5% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433294&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <img src=\"/media/181/img_splash/FleggaardAvisDK.png?5\" class=\"splashImg2 splashImg-BottomRight\">\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 159,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>159\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 21,48\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 20,20\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"6299369\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(6299369, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Grimbergen-Double-6-5%25-6x0-75-l-_2097099_40381.aspx\" title=\"Grimbergen Double 6,5% 6x0,75 l.\" class=\"productName\">Grimbergen Double 6,5% 6x0,75 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Grimbergen-Double-6-5%25-6x0-75-l-_2097099_40381.aspx\" title=\"Grimbergen Double 6,5% 6x0,75 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1332874&amp;sizeId=4317\" title=\"Grimbergen Double 6,5% 6x0,75 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1332874&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 129,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>129\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 17,45\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 28,89\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"2097099\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(2097099, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Leffe-Blonde-6-6%25-24x0-33-l-_6299368_40381.aspx\" title=\"Leffe Blonde 6,6% 24x0,33 l.\" class=\"productName\">Leffe Blonde 6,6% 24x0,33 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Leffe-Blonde-6-6%25-24x0-33-l-_6299368_40381.aspx\" title=\"Leffe Blonde 6,6% 24x0,33 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433292&amp;sizeId=4317\" title=\"Leffe Blonde 6,6% 24x0,33 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433292&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <img src=\"/media/181/img_splash/FleggaardAvisDK.png?5\" class=\"splashImg2 splashImg-BottomRight\">\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 159,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>159\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 21,48\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 20,20\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"6299368\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(6299368, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Ale-No-4-5-5%25-24x0-33l_7564534_40381.aspx\" title=\"Ale No 4 5,5% 24x0,33l\" class=\"productName\">Ale No 4 5,5% 24x0,33l</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Ale-No-4-5-5%25-24x0-33l_7564534_40381.aspx\" title=\"Ale No 4 5,5% 24x0,33l\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1631768&amp;sizeId=4317\" title=\"Ale No 4 5,5% 24x0,33l\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1631768&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <img src=\"/media/181/img_splash/FleggaardAvisDK.png?5\" class=\"splashImg2 splashImg-BottomRight\">\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 79,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>79\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 10,74\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 10,10\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"7564534\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(7564534, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Jacobsen-India-Pale-Ale-6-x-0-75-l-_7605078_40381.aspx\" title=\"Jacobsen India Pale Ale 6 x 0,75 l.\" class=\"productName\">Jacobsen India Pale Ale 6 x 0,75 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Jacobsen-India-Pale-Ale-6-x-0-75-l-_7605078_40381.aspx\" title=\"Jacobsen India Pale Ale 6 x 0,75 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1634302&amp;sizeId=4317\" title=\"Jacobsen India Pale Ale 6 x 0,75 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1634302&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 139,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>139\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 18,79\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 31,11\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"notbuyable\" title=\"not buyable\">\n" +
                "    Kan ikke forudbestilles\n" +
                "    <br> eller har lav beholdning\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Jacobsen-Brown-Ale-6%25-6x0-75-l-_4103948_40381.aspx\" title=\"Jacobsen Brown Ale 6% 6x0,75 l.\" class=\"productName\">Jacobsen Brown Ale 6% 6x0,75 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Jacobsen-Brown-Ale-6%25-6x0-75-l-_4103948_40381.aspx\" title=\"Jacobsen Brown Ale 6% 6x0,75 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1244989&amp;sizeId=4317\" title=\"Jacobsen Brown Ale 6% 6x0,75 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1244989&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <img src=\"/media/181/img_splash/FleggaardAvisDK.png?5\" class=\"splashImg2 splashImg-BottomRight\">\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 129,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>129\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 17,45\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 28,89\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"4103948\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(4103948, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Leffe-Blonde-6-6%25-6x0-75-l-_6299371_40381.aspx\" title=\"Leffe Blonde 6,6% 6x0,75 l.\" class=\"productName\">Leffe Blonde 6,6% 6x0,75 l.</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Leffe-Blonde-6-6%25-6x0-75-l-_6299371_40381.aspx\" title=\"Leffe Blonde 6,6% 6x0,75 l.\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433297&amp;sizeId=4317\" title=\"Leffe Blonde 6,6% 6x0,75 l.\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1433297&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 129,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>129\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 17,45\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 28,89\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"6299371\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(6299371, 1,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Duvel-8-5%25-0-33-l-pant_5331613_40381.aspx\" title=\"Duvel 8,5% 0,33 l. + pant\" class=\"productName\">Duvel 8,5% 0,33 l. + pant</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Duvel-8-5%25-0-33-l-pant_5331613_40381.aspx\" title=\"Duvel 8,5% 0,33 l. + pant\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1319876&amp;sizeId=4317\" title=\"Duvel 8,5% 0,33 l. + pant\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1319876&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 14,99\n" +
                "    </div>\n" +
                "    <div class=\"familyPriceAmount\">\n" +
                "     Ta' 3 for\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>39\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 5,37\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. v/merkøb DKK 40,39\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"5331613\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(5331613, 3,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Jacobsen-Golden-Naked-6x0-75l_7573141_40381.aspx\" title=\"Jacobsen Golden Naked 6x0,75l\" class=\"productName\">Jacobsen Golden Naked 6x0,75l</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Jacobsen-Golden-Naked-6x0-75l_7573141_40381.aspx\" title=\"Jacobsen Golden Naked 6x0,75l\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1631728&amp;sizeId=4317\" title=\"Jacobsen Golden Naked 6x0,75l\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=1631728&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 139,99\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>139\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 18,79\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. DKK 31,11\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"notbuyable\" title=\"not buyable\">\n" +
                "    Kan ikke forudbestilles\n" +
                "    <br> eller har lav beholdning\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>\n" +
                "  <div class=\"product\" style=\"visibility: visible;\">\n" +
                "   <a href=\"/pi/Kilkenny-4-2%25-0-33-l-pant_1210936_40381.aspx\" title=\"Kilkenny 4,2% 0,33 l. + pant\" class=\"productName\">Kilkenny 4,2% 0,33 l. + pant</a>\n" +
                "   <div class=\"productImage\">\n" +
                "    <a href=\"/pi/Kilkenny-4-2%25-0-33-l-pant_1210936_40381.aspx\" title=\"Kilkenny 4,2% 0,33 l. + pant\"><img data-src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=124624&amp;sizeId=4317\" title=\"Kilkenny 4,2% 0,33 l. + pant\" width=\"160\" height=\"160\" src=\"https://www.fleggaard.dk/Services/ImageHandler.ashx?imgId=124624&amp;sizeId=4317\"></a>\n" +
                "   </div>\n" +
                "   <div class=\"productPriceArea\">\n" +
                "    <div class=\"productOnePrice\">\n" +
                "     1 stk DKK 9,99\n" +
                "    </div>\n" +
                "    <div class=\"familyPriceAmount\">\n" +
                "     Ta' 3 for\n" +
                "    </div>\n" +
                "    <div class=\"productPrice\">\n" +
                "     <span class=\"currency\">DKK</span>24\n" +
                "     <sup>99</sup>\n" +
                "    </div>\n" +
                "    <div class=\"productPriceEuro\">\n" +
                "     € 3,35\n" +
                "    </div>\n" +
                "    <div class=\"productUnitPrice\">\n" +
                "     Pris pr. l. v/merkøb DKK 25,24\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <div class=\"mobileAddToBasket\" data-esellerid=\"1210936\">\n" +
                "    <div class=\"mobileAddToBasketButton\" onclick=\"addToBasket(1210936, 3,false)\"></div>\n" +
                "   </div>\n" +
                "   <div class=\"productAddToFavorites\"></div>\n" +
                "  </div>]]\n" +
                " </body>\n" +
                "</html>");

        Elements articles = doc.getElementsByClass("product");

        ArrayList<DrinkEntity> drinks = new ArrayList<>();
         articles.forEach(article -> {
            System.err.println(article.getElementsByClass("productname").text() + "\n" +
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
        Elements articles = doc.getElementsByClass("product-search");

        System.out.println(articles);
    }

    private DrinkEntity makeDrink(Element article, String type, String subtype) {
        String name = extractNameFromText(article);
        float alcohol = extractAlcoholFromText(article);
        float volume = extractVolumeFromText(article) * 1000; //Product volume comes in litres
        float price = extractPriceFromText(article) * currencyExchangeRate;

        float pricePerLitre = 1000 / volume * price;

        return new DrinkEntity(name, type, subtype, price, pricePerLitre, alcohol, volume, fleggaard);
    }

    private float extractPriceFromText(Element article) {
        String priceTagText = article.getElementsByClass("productOnePrice").get(0).text();
        int dkkIndex = priceTagText.indexOf("DKK ");
        int sekIndex = priceTagText.indexOf("SEK ");

        String priceString = priceTagText.substring(dkkIndex + sekIndex + 5); //One of the indexes will be -1, allows both SEK and DKK parsing

        priceString = priceString.replace(".", "").replace(",", ".");

        float returnNumber = 0f;
        try {returnNumber = Float.parseFloat(priceString);
        } catch (NumberFormatException e){
            System.err.println(priceTagText + " " + priceString);
        }

        return returnNumber;
    }

    private String extractNameFromText(Element article) {
        String nameString = article.getElementsByClass("productname").text();
        int percIndex = nameString.indexOf("%");
        int commaIndex = nameString.indexOf(",");
        if(percIndex > 0 && percIndex > commaIndex) {
            return nameString.substring(0, percIndex - 4).trim();
        } else if(percIndex > 0 && commaIndex > percIndex) {
            return nameString.substring(0, commaIndex - 6).trim();
        } else if(percIndex == -1 && commaIndex > 0) {
            return nameString.substring(0, commaIndex - 4);
        }

        return "";
    }

    private float extractAlcoholFromText(Element article) {
        String alkoholhalt = article.getElementsByClass("productname").text();
        int percIndex = alkoholhalt.indexOf("%");
        int minIndex = percIndex - 4;
        if(minIndex >= 0) {
            String substring = alkoholhalt.substring(minIndex, percIndex)
                    .trim()
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö %\"]", "")
                    .replace("-", "")
                    .replaceAll(",", ".");
            float alcohol = Float.parseFloat(substring);
            if(alcohol < 100) {
                return Float.parseFloat(substring);
            }
        }
        return 0f;
    }

    private float extractVolumeFromText(Element article) {
        String volumeString = article.getElementsByClass("productname").text();
        int percIndex = volumeString.indexOf("%");
        String substring = volumeString.substring(percIndex + 1);
        int xIndex = substring.indexOf("x");
        int lIndex = substring.lastIndexOf("l");
        float packMultiplier = 1f;
        if(percIndex >= 0) {
            packMultiplier = multiPackMultiplier(substring.substring(0, xIndex + 1), substring);
        } else if (percIndex == -1) {
            return 0f;
        }
        if(xIndex >= 0 && lIndex > 0) {
            substring = substring.substring(xIndex, lIndex)
                    .trim()
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö %]", "")
                    .replaceAll("[,]", ".");
            return Float.parseFloat(substring) * packMultiplier;
        } if(xIndex == -1 && lIndex >= 0) {
            substring = substring.substring(0, lIndex)
                    .replace("ø", "ö")
                    .replaceAll("[a-öA-Ö. ]", "")
                    .replaceAll(",", ".");
            return Float.parseFloat(substring) * packMultiplier;
        }

        return 0f;
    }

    private float multiPackMultiplier(String multiString, String multipackStringWithVolume) {
        int lIndex = multipackStringWithVolume.lastIndexOf('l');
        char volumePrefix = ' ';
        if(lIndex > 0) {
            volumePrefix = multipackStringWithVolume.charAt(lIndex - 1);
        }
        if(volumePrefix == 'c') {
            return Float.parseFloat(multiString.replaceAll("[a-zA-Z]", "").trim()) / 100;
        } else if(volumePrefix == 'm') {
            return Float.parseFloat(multiString.replaceAll("[a-zA-Z]", "").trim()) / 1000;
        } else if(multiString.contains("x")) {
            return Float.parseFloat(multiString.replaceAll("x", "").trim());
        }
        return 1f;
    }

    private boolean isMultiPack() {


        return false;
    }

    private ArrayList<DrinkEntity> scrapeAllDrinks() throws IOException {
        ArrayList<DrinkEntity> drinks = new ArrayList<>();

        drinks.addAll(scrapeDrinks("Öl", "Ale", "https://www.fleggaard.dk/pl/%C3%96l-Ale_40381.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Klassisk", "https://www.fleggaard.dk/pl/%C3%96l-Klassik_40376.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Guld", "https://www.fleggaard.dk/pl/%C3%96l-Guld%C3%B6l_40377.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Veteöl", "https://www.fleggaard.dk/pl/%C3%96l-Vete%C3%B6l_40384.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Lager", "https://www.fleggaard.dk/pl/%C3%96l-Lager_40383.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Pilsner", "https://www.fleggaard.dk/pl/%C3%96l-Pilsner_40375.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Stout", "https://www.fleggaard.dk/pl/%C3%96l-Porter---Stout_40382.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Special", "https://www.fleggaard.dk/pl/%C3%96l-Special%C3%B6l_57644.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Öl", "Svensk", "https://www.fleggaard.dk/pl/%C3%96l-Svensk-%C3%B6l_40379.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Alkoläsk och cider", "Cider", "https://www.fleggaard.dk/pl/Cider_40514.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Vitt vin", "https://www.fleggaard.dk/pl/Vin-Hvidvin_40408.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Rött vin", "https://www.fleggaard.dk/pl/Vin-R%C3%B8dvin_40407.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Rosévin", "https://www.fleggaard.dk/pl/Vin-Ros%C3%A9vin_287395.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Champagne", "https://www.fleggaard.dk/pl/Vin-Champagne_287401.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Mousserande", "https://www.fleggaard.dk/pl/Vin-Mousserende_287398.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Dessertvin", "https://www.fleggaard.dk/pl/Vin-Dessertvin_287406.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Vin", "Portvin", "https://www.fleggaard.dk/pl/Vin-Portvin_287410.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Absint", "https://www.fleggaard.dk/pl/Sprit-Absint_40392.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Akvavit", "https://www.fleggaard.dk/pl/Sprit-Akvavit---Snaps_40393.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Bitter", "https://www.fleggaard.dk/pl/Sprit-Bitter_40394.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Brandy", "https://www.fleggaard.dk/pl/Sprit-Brandy_40396.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Calvados", "https://www.fleggaard.dk/pl/Sprit-Calvados_40397.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Cognac", "https://www.fleggaard.dk/pl/Sprit-Cognac_40398.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Creme likör", "https://www.fleggaard.dk/pl/Sprit-Creme-Lik%C3%B6r_287403.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Brännvin", "https://www.fleggaard.dk/pl/Sprit-Olika-Br%C3%A4nnvin_287405.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Drinkmix", "https://www.fleggaard.dk/pl/Sprit-Drink-Mix_287407.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Gin", "https://www.fleggaard.dk/pl/Sprit-Gin_40399.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Glögg", "https://www.fleggaard.dk/pl/Sprit-Gl%C3%B6gg_287409.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Grappa", "https://www.fleggaard.dk/pl/Sprit-Grappa_40400.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Likör", "https://www.fleggaard.dk/pl/Sprit-Lik%C3%B6r_40395.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Ready to drink", "https://www.fleggaard.dk/pl/Sprit-Ready-to-Drink_40517.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Rom", "https://www.fleggaard.dk/pl/Sprit-Rom_40401.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Sherry", "https://www.fleggaard.dk/pl/Sprit-Sherry_287412.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Shots", "https://www.fleggaard.dk/pl/Sprit-Shots_40402.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Tequila", "https://www.fleggaard.dk/pl/Sprit-Tequila_40403.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Vermouth", "https://www.fleggaard.dk/pl/Sprit-Vermouth_287413.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Vodka", "https://www.fleggaard.dk/pl/Sprit-Vodka_40404.aspx?locId=732"));
        drinks.addAll(scrapeDrinks("Sprit", "Whisky", "https://www.fleggaard.dk/pl/Sprit-Whisky_40405.aspx?locId=732"));

        return drinks;
    }
}
