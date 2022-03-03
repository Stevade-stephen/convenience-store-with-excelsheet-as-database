package com.conveniencestore.models;


import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> productsInCart;
    private double totalCostOfProductsInCart;

    public Cart() {
        this.productsInCart = new HashMap<>();
    }

    public Map<Product, Integer> getProductsInCart() {
        return productsInCart;
    }

    public void setTotalCostOfProductsInCart(double totalCostOfProductsInCart) {
        this.totalCostOfProductsInCart = totalCostOfProductsInCart;
    }

    public double getTotalCostOfProductsInCart() {
        return totalCostOfProductsInCart;
    }
}
