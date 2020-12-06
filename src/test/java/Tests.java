import Exceptions.StickerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
}