package tests;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class AddingItemToCart extends TestBase {
    @Test
    @UseDataProvider(value = "validProducts", location = DataProviders.class)
    public void testAddingAndRemoveItemFromCart(Product product) {
        for (int i = 0; i < 3 ; i++) {
            app.addProductToCart(product);
        }
        app.goToCart();
        app.removeProductFromCart();
    }
}
