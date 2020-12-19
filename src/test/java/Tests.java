import Exceptions.LogBrowserException;
import Exceptions.ProductException;
import Exceptions.SortException;
import Exceptions.StickerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/ruathn7/Documents/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testCheckList() {
        driver.get("http://192.168.64.2/litecart/admin");
        loginAdminPanel();

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
    public void testCheckStickers() {
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
        loginAdminPanel();

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
        loginAdminPanel();

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

    @Test
    public void testUserRegistration() {
        String randomNumbers = generateNumbers(10);

        driver.get("http://192.168.64.2/litecart/en/");
        driver.findElement(By.cssSelector("form[name=login_form] tr:nth-child(5)")).click();

        // Создание нового аккаунта и автоматический вход
        driver.findElement(By.cssSelector("input[name=tax_id]")).sendKeys("908234432");
        driver.findElement(By.cssSelector("input[name=company]")).sendKeys("TestCompany");
        driver.findElement(By.cssSelector("input[name=firstname]")).sendKeys("Firstname");
        driver.findElement(By.cssSelector("input[name=lastname]")).sendKeys("Lastname");
        driver.findElement(By.cssSelector("input[name=address1]")).sendKeys("Address 123");
        driver.findElement(By.cssSelector("input[name=postcode]")).sendKeys("12345");
        driver.findElement(By.cssSelector("input[name=city]")).sendKeys("City");
        driver.findElement(By.cssSelector("span.select2-selection__rendered")).click();
        driver.findElement(By.cssSelector("input.select2-search__field")).sendKeys("United States");
        driver.findElement(By.cssSelector("li.select2-results__option:first-child")).click();
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(randomNumbers + "@google.com");
        driver.findElement(By.cssSelector("input[name=phone]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("input[name=confirmed_password]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("button[name=create_account]")).click();
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("input[name=confirmed_password]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("button[name=create_account]")).click();

        //Выход из аккаунта
        driver.findElement(By.cssSelector("ul.list-vertical > li:nth-child(5) a")).click();

        //Повторный вход в аккаунт
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(randomNumbers + "@google.com");
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys(randomNumbers);
        driver.findElement(By.cssSelector("button[name=login]")).click();

        //Повторный выход
        driver.findElement(By.cssSelector("ul.list-vertical > li:nth-child(5) a")).click();
    }

    @Test
    public void testAddNewProduct() throws IOException {
        String nameNewProduct = "TestProduct" + generateNumbers(10);

        driver.get("http://192.168.64.2/litecart/admin");
        loginAdminPanel();

        driver.findElement(By.cssSelector("ul#box-apps-menu li:nth-child(2) a")).click();
        driver.findElement(By.cssSelector("td#content > div > a.button:nth-child(2)")).click();

        // Заполнение информации на вкладке "General"
        driver.findElement(By.cssSelector("input[name=status][value='1']")).click();
        driver.findElement(By.cssSelector("input[name='name[en]']")).sendKeys(nameNewProduct);
        driver.findElement(By.cssSelector("input[name=code]")).sendKeys("123");
        driver.findElement(By.cssSelector("div.input-wrapper tr:nth-child(4) td:nth-child(1)")).click();
        driver.findElement(By.cssSelector("input[name=quantity]")).clear();
        driver.findElement(By.cssSelector("input[name=quantity]")).sendKeys("10");
        driver.findElement(By.cssSelector("input[type=file]"))
                .sendKeys(new File("../selenium2/src/test/java/resources/cat.png").getCanonicalPath());
        driver.findElement(By.cssSelector("input[name=date_valid_from]")).sendKeys("2022-11-12");
        driver.findElement(By.cssSelector("input[name=date_valid_to]")).sendKeys("2022-11-12");

        // Заполнение информации на вкладке "Information"
        driver.findElement(By.cssSelector("ul.index li:nth-child(2)")).click();
        driver.findElement(By.cssSelector("select[name=manufacturer_id]")).click();
        driver.findElement(By.cssSelector("select[name=manufacturer_id] option[value='1']")).click();
        driver.findElement(By.cssSelector("input[name=keywords]")).sendKeys("keywords");
        driver.findElement(By.cssSelector("input[name='short_description[en]']")).sendKeys("short description");
        driver.findElement(By.cssSelector("div.trumbowyg-editor")).sendKeys("description");
        driver.findElement(By.cssSelector("input[name='head_title[en]']")).sendKeys("head title");
        driver.findElement(By.cssSelector("input[name='meta_description[en]']")).sendKeys("meta description");

        // Заполнение информации на вкладке "Prices"
        driver.findElement(By.cssSelector("ul.index li:nth-child(4)")).click();
        driver.findElement(By.cssSelector("input[name=purchase_price]")).clear();
        driver.findElement(By.cssSelector("input[name=purchase_price]")).sendKeys("10");
        driver.findElement(By.cssSelector("select[name=purchase_price_currency_code] option[value='USD']")).click();
        driver.findElement(By.cssSelector("input[name='prices[USD]']")).sendKeys("11");
        driver.findElement(By.cssSelector("input[name='prices[EUR]']")).sendKeys("12");
        driver.findElement(By.cssSelector("input[name='gross_prices[USD]']")).clear();
        driver.findElement(By.cssSelector("input[name='gross_prices[USD]']")).sendKeys("1");
        driver.findElement(By.cssSelector("input[name='gross_prices[EUR]']")).clear();
        driver.findElement(By.cssSelector("input[name='gross_prices[EUR]']")).sendKeys("2");

        // Сохранение данных по новому товару
        driver.findElement(By.cssSelector("button[name=save]")).click();

        // Проверка, что товар появился в каталоге
        int sizeList = driver.findElements(By.cssSelector("table.dataTable a")).size();

        for (int i = 0; i < sizeList; i++) {
            if (driver.findElements(By.cssSelector("table.dataTable a")).get(i).getText().equals(nameNewProduct)) {
                return;
            }
        }

        throw new ProductException("Товар не был добавлен в каталог");
    }

    @Test
    public void testAddingAndRemoveItemFromCart() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://192.168.64.2/litecart/en/");
        int quantityInCart = driver.findElements(By.cssSelector("table.dataTable td.item")).size();;

        // Добавление товаров, пока в корзине не будет 3 товаров
        for (int i = 0; i < 3 - quantityInCart; i++) {
            // Переход на страницу товара
            driver.findElement(By.cssSelector("div.content li.product")).click();
            int quantityBeforeAdding = Integer.parseInt(driver.findElement(By.cssSelector("div#cart span.quantity")).getText());

            // Выбор размера товара, если есть такая возможность
            if (driver.findElements(By.cssSelector("[name='options[Size]']")).size() > 0) {
                driver.findElement(By.cssSelector("[name='options[Size]'] option[value=Small]")).click();
            }
            // Добавление товара в корзину
            driver.findElement(By.cssSelector("button[name=add_cart_product]")).click();

            // Ожидание, кол-во товара в корзине должно увеличится на единицу
            wait.until(ExpectedConditions.textToBe(By.cssSelector("div#cart span.quantity"), String.valueOf(quantityBeforeAdding + 1)));

            driver.get("http://192.168.64.2/litecart/en/");
        }

        // Переход в корзину
        driver.findElement(By.cssSelector("div#cart a.link")).click();

        // Получение текущего кол-ва товаров в корзине
        quantityInCart = driver.findElements(By.cssSelector("table.dataTable td.item")).size();

        // Удаление товаров по одному из корзины
        for (int i = 0; i < quantityInCart; i++) {
            int quantityBeforeDeletion = driver.findElements(By.cssSelector("table.dataTable td.item")).size();
            WebElement lastItemInTheTable =  driver.findElements(By.cssSelector("table.dataTable td.item")).get(quantityBeforeDeletion - 1);

            // Удаление товара
            driver.findElements(By.cssSelector("button[name=remove_cart_item]")).get(0).click();

            // Ожидание, пока товар не исчезнет из таблицы
            wait.until(ExpectedConditions.invisibilityOf(lastItemInTheTable));
        }
    }

    @Test
    public void testOpenLinksInNewWindow() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://192.168.64.2/litecart/admin");
        loginAdminPanel();

        driver.get("http://192.168.64.2/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.cssSelector("table.dataTable tr:nth-child(2) a[title='Edit']")).click();

        int sizeListLinks = driver.findElements(By.cssSelector("i.fa-external-link")).size();

        for (int i = 0; i < sizeListLinks; i++) {
            String mainWindow = driver.getWindowHandle();
            Set<String> oldWindows = driver.getWindowHandles();
            driver.findElements(By.cssSelector("i.fa-external-link")).get(i).click();
            String newWindow = wait.until(thereIsWindowOtherThan(oldWindows));
            driver.switchTo().window(newWindow);
            driver.close();
            driver.switchTo().window(mainWindow);
        }
    }

    @Test
    public void testCheckMessagesInLog() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://192.168.64.2/litecart/admin");
        loginAdminPanel();

        driver.get("http://192.168.64.2/litecart/admin/?app=catalog&doc=catalog&category_id=1");

        int sizeListProducts = driver.findElements(By.cssSelector("table.dataTable tr.row a:nth-child(2)")).size();

        for (int i = 1; i < sizeListProducts; i++) {
            driver.findElements(By.cssSelector("table.dataTable tr.row a:nth-child(2)")).get(i).click();

            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("div.tabs"))));

            List<LogEntry> logEntry = driver.manage().logs().get("browser").getAll();
            if (logEntry.size() > 0) {
                for (LogEntry l : logEntry) {
                    System.out.println(l);
                }
                throw new LogBrowserException("В логах браузера есть сообщения, пожалуйста, просмотрите их");
            }

            driver.get("http://192.168.64.2/litecart/admin/?app=catalog&doc=catalog&category_id=1");
        }
    }

    private String generateNumbers(int count) {
        final Random random = new Random();
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < count; i++){
            result.append(random.nextInt(10));
        }

        return result.toString();
    }

    private void loginAdminPanel() {
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
    }

    private ExpectedCondition<String> thereIsWindowOtherThan(final Set<String> oldWindows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver driver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
}