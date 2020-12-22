package tests;

import com.tngtech.java.junit.dataprovider.DataProvider;
import model.Product;

public class DataProviders {

    @DataProvider
    public static Object[][] validProducts() {
        return new Object[][] {
                { Product.newEntity()
                        .withSize("Small").build() },
                /* ... */
        };
    }

}