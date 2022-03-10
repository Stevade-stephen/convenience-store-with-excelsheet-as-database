package com.conveniencestore.multiselling;

import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Staff;
import com.conveniencestore.models.Store;
import com.conveniencestore.storeoperations.StoreOperation;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class MultipleSelling {
    private final StoreOperation storeOperation;
    private ExecutorService executorService;

    public MultipleSelling(StoreOperation storeOperation, ExecutorService executorService) {
        this.storeOperation = storeOperation;
        this.executorService = executorService;
    }

    public void multipleSelling (Store store, Set<Customer> customerList, Staff staff, int numberOfThreads){
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        Function<Customer, String> customerReceipt = customer -> storeOperation.sellProducts(store, customer, staff);
    }
}
