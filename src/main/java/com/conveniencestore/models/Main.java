package com.conveniencestore.models;

import com.conveniencestore.customer.CustomerService;
import com.conveniencestore.customer.CustomerServiceImpl;
import com.conveniencestore.enums.Gender;
import com.conveniencestore.enums.Role;
import com.conveniencestore.multiselling.MultipleSelling;
import com.conveniencestore.storeoperations.StoreOperation;
import com.conveniencestore.storeoperations.StoreOperationImpl;

import java.util.Arrays;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        Store sdtStores = new Store("sdt stores");
        Staff sola = new Staff("Sola", "Smith", "Tech Park, Edo", Gender.MALE, Role.MANAGER);
        Staff jane = new Staff("Jane", "Orlu", "Tech Park, Edo", Gender.FEMALE, Role.CASHIER);

        System.out.println(Arrays.toString(sdtStores.getListOfProductsInStore()));

        StoreOperation storeOperation = StoreOperationImpl.getStoreOperationImplInstance();
        System.out.println(Arrays.toString(storeOperation.viewProductsByCategory(sdtStores, "Shoes")));

        Customer samuel = new Customer("Samuel", "Adjei", "brooks street uyo", Gender.MALE);
        Customer mike = new Customer("Mike", "Adjei", "town street uyo", Gender.MALE);
        Customer robin = new Customer("Robin", "Rob", "city street uyo", Gender.MALE);
        Customer roy = new Customer("Roy", "Gin", "gin street uyo", Gender.MALE);

        CustomerService customerMenu = new CustomerServiceImpl();
        customerMenu.addProductsToCart(samuel, sdtStores, "Lewis", 4);
        customerMenu.addProductsToCart(robin, sdtStores, "Lewis", 4);
        customerMenu.addProductsToCart(mike, sdtStores, "Lewis", 2);
        customerMenu.fundWallet(samuel, 1000000.00);
        customerMenu.fundWallet(robin, 1000000.00);
        customerMenu.fundWallet(mike, 1000000.00);

//        System.out.println(storeOperation.sellProducts(sdtStores,samuel,jane));
        MultipleSelling sell = new MultipleSelling();
        sell.multipleSelling(sdtStores, Set.of(samuel, robin, mike), jane, 2);
    }
}
