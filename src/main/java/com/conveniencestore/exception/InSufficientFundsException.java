package com.conveniencestore.exception;

public class InSufficientFundsException extends RuntimeException {
    public InSufficientFundsException(String message) {
        super(message);
    }
}
