package com.conveniencestore.models;

import com.conveniencestore.enums.Gender;

public class Customer extends Person {
    private final Cart cart;
    private final Wallet wallet;

    public Customer(String firstName, String secondName, String address, Gender gender) {
        super(firstName, secondName, address, gender);
        this.cart = new Cart();
        this.wallet = new Wallet();
    }

    public Cart getCart() {
        return cart;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
