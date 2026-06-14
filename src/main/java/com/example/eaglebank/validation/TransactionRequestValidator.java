package com.example.eaglebank.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.eaglebank.exception.BadRequestException;
import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;
import com.example.eaglebank.model.json.CreateTransactionRequest;

@Component
public class TransactionRequestValidator {
    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("10000.00");
    private static final Pattern TRANSACTION_ID_PATTERN = Pattern.compile("^tan-[A-Za-z0-9]+$");

    public void validateTransactionId(final String transactionId) {
        final List<ValidationError> details = new ArrayList<>();
        if (isBlank(transactionId) || !TRANSACTION_ID_PATTERN.matcher(transactionId).matches()) {
            details.add(new ValidationError("transactionId", "must match tan-[A-Za-z0-9]+", "format"));
        }
        throwIfInvalid(details);
    }

    public void validateCreateTransaction(final CreateTransactionRequest request) {
        final List<ValidationError> details = new ArrayList<>();
        if (request == null) {
            details.add(new ValidationError("body", "request body is required", "required"));
            throwIfInvalid(details);
        }

        validateAmount(request.getAmount(), details);
        if (!"GBP".equals(request.getCurrency())) {
            details.add(new ValidationError("currency", "must be GBP", "invalid"));
        }
        if (!"deposit".equals(request.getType()) && !"withdrawal".equals(request.getType())) {
            details.add(new ValidationError("type", "must be deposit or withdrawal", "invalid"));
        }

        throwIfInvalid(details);
    }

    private void validateAmount(final BigDecimal amount, final List<ValidationError> details) {
        if (amount == null) {
            details.add(new ValidationError("amount", "is required", "required"));
            return;
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            details.add(new ValidationError("amount", "must be between 0.00 and 10000.00", "range"));
        }
        if (amount.scale() > 2) {
            details.add(new ValidationError("amount", "must have no more than two decimal places", "format"));
        }
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
