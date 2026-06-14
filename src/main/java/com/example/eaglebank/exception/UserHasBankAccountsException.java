package com.example.eaglebank.exception;

public class UserHasBankAccountsException extends RuntimeException {
    public UserHasBankAccountsException() {
        super("A user cannot be deleted when they are associated with a bank account");
    }
}
