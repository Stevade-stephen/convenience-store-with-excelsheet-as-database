package com.conveniencestore.customer;

import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Product;
import com.conveniencestore.models.Store;

public interface CustomerService {
    void addProductsToCart(Customer customer, Store store, String productName, int quantity);
    String fundWallet(Customer customer, double amount);
}
