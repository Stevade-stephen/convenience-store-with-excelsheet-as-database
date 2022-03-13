package com.conveniencestore.multiselling;

import com.conveniencestore.customer.CustomerService;
import com.conveniencestore.customer.CustomerServiceImpl;
import com.conveniencestore.enums.Gender;
import com.conveniencestore.enums.ProductAvailability;
import com.conveniencestore.enums.Role;
import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Product;
import com.conveniencestore.models.Staff;
import com.conveniencestore.models.Store;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MultipleSellingTest {
    private MultipleSelling multipleSelling;
    private Store sdtStores;
    private Staff cashier;
    private CustomerService customerMenu;
    private Customer samuel;
    private Customer mike;
    private Customer robin;
    private Customer roy;

    public MultipleSellingTest(){

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeEach
    public void beforeAll(){
        multipleSelling = new MultipleSelling();
        customerMenu = new CustomerServiceImpl();
        sdtStores = new Store("Stevade Collections");
        cashier = new Staff("Sola", "Smith", "Tech Park, Edo", Gender.MALE, Role.CASHIER);
        samuel = new Customer("Samuel", "Adjei", "brooks street uyo", Gender.MALE);
        mike = new Customer("Mike", "Adjei", "town street uyo", Gender.MALE);
        robin = new Customer("Robin", "Rob", "city street uyo", Gender.MALE);
        roy = new Customer("Roy", "Gin", "gin street uyo", Gender.MALE);

        customerMenu.fundCustomerWallet(samuel, 1000000.00);
        customerMenu.fundCustomerWallet(robin, 1000000.00);
        customerMenu.fundCustomerWallet(mike, 1000000.00);
        customerMenu.fundCustomerWallet(roy, 1000000.00);

    }

    @Test()
    @DisplayName("TestingConcurrentSellingForSameProducts")
    void canSellToMultipleCustomersBuyingTheSameProductConcurrentlyAndCorrectly(){
        customerMenu.addProductsToCart(samuel, sdtStores, "Lewis", 4);
        customerMenu.addProductsToCart(robin, sdtStores, "Lewis", 4);
        customerMenu.addProductsToCart(mike, sdtStores, "Lewis", 2);
        customerMenu.addProductsToCart(roy, sdtStores, "Lewis", 1);

        multipleSelling.multipleSelling(sdtStores, cashier, 2);

        Product[] listOfProductsInStore = sdtStores.getListOfProductsInStore();
        Function<Product[], Product> findProduct = (products) ->
                Arrays.stream(products)
                        .filter(product -> product.getName().equalsIgnoreCase("Lewis"))
                        .collect(Collectors.toList()).get(0);

        Product productInStore = findProduct.apply(listOfProductsInStore);
        assertThat(productInStore.getProductAvailability()).isEqualTo(ProductAvailability.OUT_OF_STOCK);
        assertThat(productInStore.getQuantity()).isZero();
        assertThat(sdtStores.getMultithreadedList().size()).isZero();

    }

    @Test
    @DisplayName("TestingConcurrentSellingForDifferentProducts")
    void CanSellToMultipleCustomersBuyingDifferentProductConcurrentlyAndCorrectly(){
        customerMenu.addProductsToCart(samuel, sdtStores, "Vans", 4);
        customerMenu.addProductsToCart(robin, sdtStores, "Lewis", 4);
        customerMenu.addProductsToCart(mike, sdtStores, "Gucci", 2);
        customerMenu.addProductsToCart(roy, sdtStores, "Polo", 1);

        multipleSelling.multipleSelling(sdtStores, cashier, 2);

        Function<Product[], Product> findLewis = (products) ->
                Arrays.stream(products)
                        .filter(product -> product.getName().equalsIgnoreCase("Lewis"))
                        .collect(Collectors.toList()).get(0);

        Function<Product[], Product> findVans = (products) ->
                Arrays.stream(products)
                        .filter(product -> product.getName().equalsIgnoreCase("Vans"))
                        .collect(Collectors.toList()).get(0);

        Function<Product[], Product> findPolo = (products) ->
                Arrays.stream(products)
                        .filter(product -> product.getName().equalsIgnoreCase("Polo"))
                        .collect(Collectors.toList()).get(0);

        Function<Product[], Product> findGucci = (products) ->
                Arrays.stream(products)
                        .filter(product -> product.getName().equalsIgnoreCase("Lewis"))
                        .collect(Collectors.toList()).get(0);

        Product gucci = findGucci.apply(sdtStores.getListOfProductsInStore());
        Product polo = findPolo.apply(sdtStores.getListOfProductsInStore());
        Product vans = findVans.apply(sdtStores.getListOfProductsInStore());
        Product lewis = findLewis.apply(sdtStores.getListOfProductsInStore());

        assertThat(gucci.getQuantity()).isEqualTo(3);
        assertThat(polo.getQuantity()).isEqualTo(1);
        assertThat(vans.getQuantity()).isEqualTo(2);
        assertThat(lewis.getQuantity()).isEqualTo(3);

    }

}