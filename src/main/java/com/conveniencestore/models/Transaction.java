package com.conveniencestore.models;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private double transactionAmount;
    private List<Product> productsBoughtByCustomer;
    private Staff processedBy;
    private Customer customer;

    public Transaction(double transactionAmount, Staff processedBy, Customer customer) {
        this.transactionAmount = transactionAmount;
        this.productsBoughtByCustomer = new ArrayList<>();
        this.processedBy = processedBy;
        this.customer = customer;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public List<Product> getProductsBoughtByCustomer() {
        return productsBoughtByCustomer;
    }


    public Staff getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Staff processedBy) {
        this.processedBy = processedBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionAmount=" + transactionAmount +
                ", productsBoughtByCustomer=" + productsBoughtByCustomer +
                ", processedBy=" + processedBy +
                ", customer=" + customer +
                '}';
    }
}
