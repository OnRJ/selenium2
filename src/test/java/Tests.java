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
}