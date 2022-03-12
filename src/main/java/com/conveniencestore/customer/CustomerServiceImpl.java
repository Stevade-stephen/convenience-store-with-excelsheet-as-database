package com.conveniencestore.customer;

import com.conveniencestore.enums.ProductAvailability;
import com.conveniencestore.exception.NoSuchProductException;
import com.conveniencestore.exception.OutOfStockException;
import com.conveniencestore.exception.QuantityExceededException;
import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Product;
import com.conveniencestore.models.Store;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.conveniencestore.enums.ProductAvailability.*;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public void addProductsToCart(Customer customer, Store store, String productName , int quantity) {
        for (Product product : store.getListOfProductsInStore()) {
            if (product.getName().equalsIgnoreCase(productName)) {
                checkIfProductIsAvailableInTheStore(customer, store, quantity, product);
                break;
            }
        }
    }

    private void checkIfProductIsAvailableInTheStore(Customer customer, Store store, int quantity, Product product) {
        if (product.getProductAvailability().equals(AVAILABLE)) {
            checkIfStoreHasEnoughQuantityAndAddToCart(customer, store, quantity, product);
        }
        else throw new OutOfStockException("This product is out of stock");
    }

    private void checkIfStoreHasEnoughQuantityAndAddToCart(Customer customer, Store store, int quantity, Product product) {
        if (product.getQuantity() >= quantity) {
            customer.getCart().getProductsInCart().merge(product, quantity, Integer::sum);
            AddCustomerToMultithreadedListForMultithreading(customer, store);
            System.out.println(product.getName() + " has been added to " + customer.getFirstName() + "'s cart.");
        } else
            throw new QuantityExceededException("You cannot add up to " + quantity
                    + " to your cart, " + "only " + product.getQuantity() + " is/are left.");
    }

    private void AddCustomerToMultithreadedListForMultithreading(Customer customer, Store store) {
        store.getMultithreadedList().add(customer);
    }

    @Override
    public String fundCustomerWallet(Customer customer, double amount) {
        double newBalance = customer.getWallet().getBalance() + amount;
        customer.getWallet().setBalance(newBalance);
        return "Successfully added " + amount + " to wallet. Your new balance is " + customer.getWallet().getBalance();
    }
}
