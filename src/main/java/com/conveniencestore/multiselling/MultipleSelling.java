package com.conveniencestore.multiselling;

import com.conveniencestore.models.Customer;
import com.conveniencestore.models.Staff;
import com.conveniencestore.models.Store;
import com.conveniencestore.storeoperations.StoreOperationImpl;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MultipleSelling {
    private final StoreOperationImpl storeOperation = StoreOperationImpl.getStoreOperationImplInstance();


    public void multipleSelling(Store store, Staff staff, int numberOfThreads){
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        try {
            List<Callable<String>> sellingTasks = sellToMultipleCustomers(store, staff);

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
            System.out.println(Arrays.toString(store.getListOfProductsInStore()));
            store.getMultithreadedList().clear();
        }
    }

    private List<Callable<String>> sellToMultipleCustomers(Store store, Staff staff) {
        return store.getMultithreadedList().stream()
                .map(customer -> sellToCustomerInADifferentThread(store, customer, staff))
                .collect(Collectors.toList());
    }

    private Callable<String> sellToCustomerInADifferentThread(Store store, Customer customer, Staff staff) {
        return () -> storeOperation.sellProducts(store, customer, staff);
    }

}
