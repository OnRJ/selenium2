package app;

import model.Product;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;

public class Application {
    private WebDriverWait wait;
    private WebDriver driver;

    private CartPage cartPage;
    private MainPage mainPage;
    private ProductPage productPage;

    public Application() {
        System.setProperty("webdriver.chrome.driver", "/Users/ruathn7/Documents/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        cartPage = new CartPage(driver);
        mainPage = new MainPage(driver);
        productPage = new ProductPage(driver);
    }

    public void quit() {
        driver.quit();
    }

    public void addProductToCart(Product product) {
        mainPage.open();
        mainPage.product().click();

        int quantityBeforeAdding = productPage.getQuantityInCart();

        if (productPage.hasSize()) {
            productPage.setSize(product.getSize()).click();
        }

        productPage.addToCartButton().click();
        productPage.waitIncreaseQuantityInCart(quantityBeforeAdding + 1);
        mainPage.open();
    }

    public void goToCart() {
        mainPage.open();
        mainPage.cart().click();
    }

    public void removeProductFromCart() {
        cartPage.open();
        int quantityInCart = cartPage.getItemsCart().size();

        for (int i = 0; i < quantityInCart; i++) {
            int quantityBeforeDeletion = cartPage.getItemsCart().size();
            WebElement lastItemInTheTable = cartPage.getItemsCart().get(quantityBeforeDeletion - 1);
            cartPage.removeItemButton().click();
            wait.until(ExpectedConditions.invisibilityOf(lastItemInTheTable));
        }
    }

    public int getQuantityInCartProductPage() {
        return productPage.getQuantityInCart();
    }

    public int getQuantityInCartCartPage() {
        return cartPage.getItemsCart().size();
    }
}
