import Exceptions.SortException;
import Exceptions.StickerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {
    WebDriver driver;

    @Before
    public void setUp(){
        System.setProperty("webdriver.gecko.driver", "/Users/ruathn7/Documents/geckodriver");
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
        driver = new FirefoxDriver();
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void testCheckList() {
        driver.get("http://192.168.64.2/litecart/admin");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();

        int sizeList = driver.findElements(By.cssSelector("ul#box-apps-menu > li#app-")).size();
        for (int i = 1; i <= sizeList; i++) {
            driver.findElement(By.cssSelector("ul#box-apps-menu > li#app-:nth-child(" + i + ")")).click();
            driver.findElement(By.cssSelector("td#content > h1")).isDisplayed();

            int sizeChildsList = driver.findElements(By.cssSelector("ul#box-apps-menu > li > ul.docs li")).size();

            for (int j = 1; j <= sizeChildsList; j++) {
                driver.findElement(By.cssSelector("ul#box-apps-menu > li > ul li:nth-child(" + j + ")")).click();
                driver.findElement(By.cssSelector("td#content > h1")).isDisplayed();
            }
        }
    }

    @Test
    public void testCheckStickers(){
        driver.get("http://192.168.64.2/litecart/en/");

        // Проверка стикеров в блоке "Most Popular"
        int sizeListMostPopular = driver.findElements(By
                .cssSelector("div#box-most-popular li.product"))
                .size();

        for (int i = 1; i <= sizeListMostPopular; i++) {
            int countStickers = driver.findElements(By
                    .cssSelector("div#box-most-popular li.product:nth-child(" + i + ") div.sticker"))
                    .size();

            if (countStickers != 1){
                throw new StickerException("Неверное кол-во стикеров на товаре №" + i + " в разделе \"Most Popular\"");
            }
        }


        // Проверка стикеров в блоке "Campaigns"
        int sizeListCampaigns = driver.findElements(By
                .cssSelector("div#box-campaigns li.product"))
                .size();

        for (int i = 1; i <= sizeListCampaigns; i++) {
            int countStickers = driver.findElements(By
                    .cssSelector("div#box-campaigns li.product:nth-child(" + i + ") div.sticker"))
                    .size();

            if (countStickers != 1){
                throw new StickerException("Неверное кол-во стикеров на товаре №" + i + " в разделе \"Campaigns\"");
            }
        }


        // Проверка стикеров в блоке "Latest products"
        int sizeListLatestProducts = driver.findElements(By
                .cssSelector("div#box-latest-products li.product"))
                .size();

        for (int i = 1; i <= sizeListLatestProducts; i++) {
            int countStickers = driver.findElements(By
                    .cssSelector("div#box-latest-products li.product:nth-child(" + i + ") div.sticker"))
                    .size();

            if (countStickers != 1){
                throw new StickerException("Неверное кол-во стикеров на товаре №" + i + " в разделе \"Latest products\"");
            }
        }
    }

    @Test
    public void testCheckSortCountriesAndZones() {
        driver.get("http://192.168.64.2/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();

        // Проверка, что страны расположены в алфавитном порядке
        List<String> countries = new ArrayList<String>();
        List<WebElement> listCountries = driver.findElements(By.cssSelector("form[name=countries_form] td:nth-child(5) a"));

        for (WebElement listCountry : listCountries) {
            countries.add(listCountry.getText());
        }

        List<String> countriesForComparison = new ArrayList<String>(countries);
        Collections.sort(countriesForComparison);

        if(!countriesForComparison.equals(countries)) {
            throw new SortException("Страны расположены не в алфавитном порядке");
        }

        // Проверка, что зоны стран расположены в алфавитном порядке
        for (int i = 0; i < listCountries.size(); i++) {
            if(!driver.findElements(By.cssSelector("form[name=countries_form] td:nth-child(6)"))
                    .get(i).getAttribute("textContent")
                    .equals("0")) {

                driver.findElements(By.cssSelector("form[name=countries_form] td:nth-child(5) a")).get(i).click();

                List<String> zones = new ArrayList<String>();
                List<WebElement> listZones = driver.findElements(By.cssSelector("#table-zones td:nth-child(3) [type=hidden]"));

                for (WebElement listZone : listZones) {
                    zones.add(listZone.getAttribute("value"));
                }

                List<String> zonesForComparison = new ArrayList<String>(zones);
                Collections.sort(zonesForComparison);

                if(!zonesForComparison.equals(zones)) {
                    throw new SortException("Зоны расположены не в алфавитном порядке");
                }

                driver.get("http://192.168.64.2/litecart/admin/?app=countries&doc=countries");
            }
        }
    }

    @Test
    public void testCheckSortGeoZones() {
        driver.get("http://192.168.64.2/litecart/admin/?app=geo_zones&doc=geo_zones");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();

        // Проверка, что гео-зоны стран расположены в алфавитном порядке
        int sizeListCountries = driver.findElements(By.cssSelector("form[name=geo_zones_form] td:nth-child(3)")).size();

        for (int i = 0; i < sizeListCountries; i++) {
            driver.findElements(By.cssSelector("form[name=geo_zones_form] td:nth-child(3) a")).get(i).click();

            List<String> geoZones = new ArrayList<String>();
            List<WebElement> listGeoZones = driver.findElements(By.cssSelector("table[id=table-zones] td:nth-child(3) [selected=selected]"));

            for (WebElement listCountry : listGeoZones) {
                geoZones.add(listCountry.getText());
            }

            List<String> geoZonesForComparison = new ArrayList<String>(geoZones);
            Collections.sort(geoZonesForComparison);

            if(!geoZonesForComparison.equals(geoZones)) {
                throw new SortException("Гео-зоны стран расположены не в алфавитном порядке");
            }

            driver.get("http://192.168.64.2/litecart/admin/?app=geo_zones&doc=geo_zones");
        }
    }

    @Test
    public void testOpenPageProduct() {
        driver.get("http://192.168.64.2/litecart/en/");

        // Наименование продукта на главной странице
        String nameProductMainPage = driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) div.name"))
                .getText();

        // Старая цена продукта на главной странице
        String regularPriceMainPage = driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) s.regular-price"))
                .getText();

        // Акционная цена продукта на главной странице
        String campaignPriceMainPage = driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) strong.campaign-price"))
                .getText();

        // Размер старой цены на главной странице
        double sizeRegularPriceMainPage = Double.parseDouble(driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) s.regular-price"))
                .getCssValue("font-size").replace("px", ""));

        // Размер акционной цены на главной странице
        double sizeCampaignPriceMainPage = Double.parseDouble(driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) strong.campaign-price"))
                .getCssValue("font-size").replace("px", ""));

        // Цвет акционной цены на главной странице
        Color colorCampaignPriceMainPage = Color.fromString(driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) strong.campaign-price"))
                .getCssValue("color"));

        // Цвет старой цены на главной странице
        Color colorRegularPriceMainPage = Color.fromString(driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) s.regular-price"))
                .getCssValue("color"));

        // Получение стилей старой цены на главной странице
        String styleRegularPriceMainPage = driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) s.regular-price"))
                .getCssValue("text-decoration");

        // Получение толщины шрифта акционной цены на главной странице
        int styleCampaignPriceMainPage = Integer.parseInt(driver.findElement(By
                .cssSelector("div#box-campaigns li.product:nth-child(1) strong.campaign-price"))
                .getCssValue("font-weight"));

        // Переход на страницу товара
        driver.findElement(By.cssSelector("div#box-campaigns li.product:nth-child(1) div.name")).click();

        // Размер старой цены на странице продукта
        double sizeRegularPriceProductPage = Double.parseDouble(driver.findElement(By
                .cssSelector("div.information s.regular-price"))
                .getCssValue("font-size").replace("px", ""));

        // Размер акционной цены на странице продукта
        double sizeCampaignPriceProductPage = Double.parseDouble(driver.findElement(By
                .cssSelector("div.information strong.campaign-price"))
                .getCssValue("font-size").replace("px", ""));

        // Получение стилей старой цены на странице продукта
        String styleRegularPriceProductPage = driver.findElement(By
                .cssSelector("div.information s.regular-price"))
                .getCssValue("text-decoration");

        // Получение толщины шрифта акционной цены на главной странице
        int styleCampaignPriceProductPage = Integer.parseInt(driver.findElement(By
                .cssSelector("div.information strong.campaign-price"))
                .getCssValue("font-weight"));

        // Цвет акционной цены на странице продукта
        Color colorCampaignPriceProductPage = Color.fromString(driver.findElement(By
                .cssSelector("div.information strong.campaign-price"))
                .getCssValue("color"));

        // Цвет старой цены на главной странице
        Color colorRegularPriceProductPage = Color.fromString(driver.findElement(By
                .cssSelector("div.information s.regular-price"))
                .getCssValue("color"));

        // Проверка, что названия на главной странице и на странице продукта одинаковые
        assertEquals(nameProductMainPage, driver.findElement(By
                .cssSelector("h1.title"))
                .getText());

        // Проверка, что старая цена на главной странице и на странице продукта одинаковые
        assertEquals(regularPriceMainPage, driver.findElement(By
                .cssSelector("div.information s.regular-price"))
                .getText());

        // Проверка, что акционная цена на главной странице и на странице продукта одинаковые
        assertEquals(campaignPriceMainPage, driver.findElement(By
                .cssSelector("div.information strong.campaign-price"))
                .getText());

        // Проверка, что старая цена зачеркнута на главной странице
        assertTrue(styleRegularPriceMainPage.contains("line-through"));

        // Проверка, что старая цена зачеркнута на странице продукта
        assertTrue(styleRegularPriceProductPage.contains("line-through"));

        // Проверка цвета старой цены на главной странице
        assertTrue(colorRegularPriceMainPage.getColor().getRed()
                == colorRegularPriceMainPage.getColor().getGreen()
                && colorRegularPriceMainPage.getColor().getRed()
                == colorRegularPriceMainPage.getColor().getBlue());

        // Проверка цвета акционной цены на главной странице
        assertTrue(colorCampaignPriceMainPage.getColor().getGreen() == 0
                && colorCampaignPriceMainPage.getColor().getBlue() == 0);

        // Проверка жирности шрифта для акционной цены на главной странице
        assertTrue(styleCampaignPriceMainPage >= 700);

        // Проверка жирности шрифта для акционной цены на странице продукта
        assertTrue(styleCampaignPriceProductPage >= 700);

        // Проверка цвета старой цены на странице продукта
        assertTrue(colorRegularPriceProductPage.getColor().getRed()
                == colorRegularPriceProductPage.getColor().getGreen()
                && colorRegularPriceProductPage.getColor().getRed()
                == colorRegularPriceProductPage.getColor().getBlue());

        // Проверка цвета акционной цены на странице продукта
        assertTrue(colorCampaignPriceProductPage.getColor().getGreen() == 0
                && colorCampaignPriceProductPage.getColor().getBlue() == 0);

        // Проверка размера цен на главной странице
        assertTrue(sizeCampaignPriceMainPage > sizeRegularPriceMainPage);

        // Проверка размера цен на странице продукта
        assertTrue(sizeCampaignPriceProductPage > sizeRegularPriceProductPage);
    }
}