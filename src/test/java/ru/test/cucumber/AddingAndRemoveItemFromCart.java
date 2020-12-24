package ru.test.cucumber;

import app.Application;
import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import model.Product;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class AddingAndRemoveItemFromCart extends CucumberTestBase implements En  {

    private Product.Builder builder = Product.newEntity();
    private Product product;

    public AddingAndRemoveItemFromCart() {
        When("user added the 3 product with size {string} to the cart", (String size) -> {
            product = builder.withSize(size).build();
            for (int i = 0; i < 3; i++) {
                app.addProductToCart(product);
            }
        });
        Then("quantity increased by 3 in the cart", () -> assertEquals(3, app.getQuantityInCartProductPage()));
        When("the user went to the cart and deleted all products from it", () -> app.removeProductFromCart());
        Then("the cart is empty", () -> assertEquals(0, app.getQuantityInCartCartPage()));
    }
}
