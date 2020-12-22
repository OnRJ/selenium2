package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends Page {
    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("http://192.168.64.2/litecart/en/checkout");
    }

    public List<WebElement> getItemsCart() {
        return driver.findElements(By.cssSelector("table.dataTable td.item"));
    }

    public WebElement removeItemButton() {
        return driver.findElements(By.cssSelector("button[name=remove_cart_item]")).get(0);
    }
}
