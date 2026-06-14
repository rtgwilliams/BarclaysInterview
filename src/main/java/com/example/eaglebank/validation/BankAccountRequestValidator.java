package com.example.eaglebank.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.eaglebank.exception.BadRequestException;
import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;
import com.example.eaglebank.model.json.CreateBankAccountRequest;

@Component
public class BankAccountRequestValidator {
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^01\\d{6}$");

    public void validateAccountNumber(final String accountNumber) {
        final List<ValidationError> details = new ArrayList<>();
        if (isBlank(accountNumber) || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
            details.add(new ValidationError("accountNumber", "must match 01 followed by 6 digits", "format"));
        }
        throwIfInvalid(details);
    }

    public void validateCreateAccount(final CreateBankAccountRequest request) {
        final List<ValidationError> details = new ArrayList<>();
        if (request == null) {
            details.add(new ValidationError("body", "request body is required", "required"));
            throwIfInvalid(details);
        }

        if (isBlank(request.getName())) {
            details.add(new ValidationError("name", "is required", "required"));
        }
        if (!"personal".equals(request.getAccountType())) {
            details.add(new ValidationError("accountType", "must be personal", "invalid"));
        }

        throwIfInvalid(details);
    }

    private void throwIfInvalid(final List<ValidationError> details) {
        if (!details.isEmpty()) {
            throw new BadRequestException("Invalid details supplied", details);
        }
    }

    private boolean isBlank(final String value) {
        return value == null || value.trim().isEmpty();
    }
}
