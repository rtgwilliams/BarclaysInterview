package com.example.eaglebank.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String userId) {
        super("User was not found: " + userId);
    }
}
