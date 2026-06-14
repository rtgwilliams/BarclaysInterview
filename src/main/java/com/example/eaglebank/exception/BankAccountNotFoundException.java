package com.example.eaglebank.exception;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(final String accountNumber) {
        super("Bank account was not found: " + accountNumber);
    }
}
