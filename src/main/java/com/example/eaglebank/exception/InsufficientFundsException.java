package com.example.eaglebank.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds to process transaction");
    }
}
