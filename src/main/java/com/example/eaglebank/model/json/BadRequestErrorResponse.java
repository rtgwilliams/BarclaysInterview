package com.example.eaglebank.model.json;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestErrorResponse {
    private final String message;
    private final List<ValidationError> details;

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;
        private final String type;
    }
}
