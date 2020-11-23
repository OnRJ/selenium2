import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Tests {

    @Test
    public void testOpenLink() {
        System.setProperty("webdriver.chrome.driver", "/Users/ruathn7/Documents/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");
        driver.quit();
    }
}