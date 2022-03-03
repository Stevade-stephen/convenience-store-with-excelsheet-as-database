package com.conveniencestore.storeoperations;

import com.conveniencestore.models.*;

public interface StoreOperation {
    void hireCashier(Applicant applicant, Staff staff, Store store);
    String sellProducts(Store store, Customer customer, Staff staff);
    Product[] viewProductsByCategory(Store store,String productCategory);
}
