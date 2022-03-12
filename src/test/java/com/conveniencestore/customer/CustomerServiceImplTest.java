package com.conveniencestore.customer;

import com.conveniencestore.enums.Gender;
import com.conveniencestore.exception.QuantityExceededException;
import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerServiceImplTest {

    private CustomerService customerService;
    private Customer customer;
    private Store store;

    @BeforeEach
    public void setUp(){
        customerService = new CustomerServiceImpl();
        customer = new Customer("Stephen", "Gerald", "ETP", Gender.MALE);
        store = new Store("Stevade Collections");
    }

    @Test
    void canAddProductsToCustomerCartSuccessfully() {
        customerService.addProductsToCart(customer, store, "Lewis", 4);
        customerService.addProductsToCart(customer, store, "Lewis", 4);
        assertThat(customer.getCart().getProductsInCart().size()).isEqualTo(1);
        assertThat(customer.getCart().getProductsInCart().keySet().stream().anyMatch(product -> product.getName().equals("Lewis"))).isTrue();
        customerService.addProductsToCart(customer, store, "Vans", 4);
        assertThat(customer.getCart().getProductsInCart().size()).isEqualTo(2);

        customerService.addProductsToCart(customer, store, "Polo", 1);
        assertThat(customer.getCart().getProductsInCart().size()).isEqualTo(3);
    }

    @Test
    void shouldThrowExceptionWhenAddingMoreThanAvailableProductsInStoreToCustomerCart() {
        assertThrows(QuantityExceededException.class,
                ()-> customerService.addProductsToCart(customer, store, "Vans", 8));


        assertThrows(QuantityExceededException.class,
                ()-> customerService.addProductsToCart(customer, store, "Polo", 12));

        int quantity = 8;
        assertThatThrownBy( () ->  customerService.addProductsToCart(customer, store, "Lewis", quantity))
                .isInstanceOf(QuantityExceededException.class)
                .hasMessage("You cannot add up to " + quantity
                        + " to your cart, " + "only " + 7 + " is/are left.");
    }

    @Test
    void canFundCustomerWalletSuccessfully() {
        customerService.fundCustomerWallet(customer, 1000000.00);
        assertThat(customer.getWallet().getBalance()).isEqualTo(1000000.00);

        customerService.fundCustomerWallet(customer, 4000000.00);
        assertThat(customer.getWallet().getBalance()).isEqualTo(5000000.00);

        customerService.fundCustomerWallet(customer, 0);
        assertThat(customer.getWallet().getBalance()).isEqualTo(5000000.00);

    }
}