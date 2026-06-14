package com.example.eaglebank.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(final String transactionId) {
        super("Transaction was not found: " + transactionId);
    }
}
