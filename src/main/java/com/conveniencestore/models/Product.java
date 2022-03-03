package com.conveniencestore.models;

import com.conveniencestore.enums.ProductAvailability;
import com.conveniencestore.enums.ProductCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.conveniencestore.enums.ProductAvailability.*;

@Data
@NoArgsConstructor

public class Product {
    private String name;
    private double price;
    private int quantity;
    private ProductCategory productCategory;
    private ProductAvailability productAvailability;

    public Product(String name, double price, int quantity, ProductCategory productCategory) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productCategory = productCategory;
    }

    public void checkAndSetAvailability() {
        if (this.getQuantity() == 0)
            this.setProductAvailability(OUT_OF_STOCK);
        else this.setProductAvailability(AVAILABLE);
    }
}
