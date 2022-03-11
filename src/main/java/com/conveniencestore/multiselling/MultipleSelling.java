package com.conveniencestore.multiselling;

import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Staff;
import com.conveniencestore.models.Store;
import com.conveniencestore.storeoperations.StoreOperationImpl;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MultipleSelling {
    private final StoreOperationImpl storeOperation = StoreOperationImpl.getStoreOperationImplInstance();
    private ExecutorService executorService;


    public void multipleSelling(Store store, Set<Customer> customerList, Staff staff, int numberOfThreads){
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<String>> sellingTasks = customerList.stream().map(customer -> sellToCustomer(store, customer, staff))
                .collect(Collectors.toList());

        try {
            List<Future<String>> futures = executorService.invokeAll(sellingTasks);
            futures.forEach(
                    result -> {
                        try {
                            System.out.println(result.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
    }

    private Callable<String> sellToCustomer(Store store, Customer customer, Staff staff) {
        return () -> storeOperation.sellProducts(store, customer, staff);
    }

}
