package tests.em_projects.com.mytestapplication.billing.products;

import java.io.Serializable;

/**
 * Created by USER on 23/03/2017.
 */

public class Product implements Serializable {

    private String sku;
    private String description;
    private String price;
    private String currencyCode;
    private String title;
    private String type;

    public Product(String sku, String description, String price, String currencyCode, String title, String type) {
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.currencyCode = currencyCode;
        this.title = title;
        this.type = type;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
