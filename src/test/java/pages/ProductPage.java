package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends Page {
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public int getQuantityInCart() {
        return Integer.parseInt(driver.findElement(By.cssSelector("div#cart span.quantity")).getText());
    }

    public boolean hasSize() {
        return driver.findElements(By.cssSelector("[name='options[Size]']")).size() > 0;
    }

    public WebElement setSize(String size) {
        return driver.findElement(By.cssSelector("[name='options[Size]'] option[value=" + size + "]"));
    }

    public WebElement addToCartButton() {
        return driver.findElement(By.cssSelector("button[name=add_cart_product]"));
    }

    public void waitIncreaseQuantityInCart(int quantityAfterAdding) {
        wait.until(ExpectedConditions.textToBe(By.cssSelector("div#cart span.quantity"), String.valueOf(quantityAfterAdding)));
    }
}
