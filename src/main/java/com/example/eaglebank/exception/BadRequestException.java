package com.example.eaglebank.exception;

import java.util.List;

import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;

public class BadRequestException extends RuntimeException {
    private final List<ValidationError> details;

    public BadRequestException(final String message, final List<ValidationError> details) {
        super(message);
        this.details = details;
    }

    public List<ValidationError> getDetails() {
        return details;
    }
}
