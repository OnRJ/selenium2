package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage extends Page {
    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("http://192.168.64.2/litecart/en/");
    }

    public WebElement product() {
        return driver.findElement(By.cssSelector("div.content li.product"));
    }

    public WebElement cart() {
        return driver.findElement(By.cssSelector("div#cart a.link"));
    }
}
