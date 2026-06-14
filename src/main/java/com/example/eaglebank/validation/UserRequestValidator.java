package com.example.eaglebank.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.eaglebank.exception.BadRequestException;
import com.example.eaglebank.model.domain.Address;
import com.example.eaglebank.model.json.AuthenticationRequest;
import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;
import com.example.eaglebank.model.json.CreateUserRequest;
import com.example.eaglebank.model.json.UpdateUserRequest;

@Component
public class UserRequestValidator {
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^usr-[A-Za-z0-9]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public void validateUserId(final String userId) {
        final List<ValidationError> details = new ArrayList<>();
        if (isBlank(userId) || !USER_ID_PATTERN.matcher(userId).matches()) {
            details.add(new ValidationError("userId", "must match usr-[A-Za-z0-9]+", "format"));
        }
        throwIfInvalid(details);
    }

    public void validateCreateUser(final CreateUserRequest user) {
        final List<ValidationError> details = new ArrayList<>();
        if (user == null) {
            details.add(new ValidationError("body", "request body is required", "required"));
            throwIfInvalid(details);
        }

        requireString(user.getName(), "name", details);
        requireAddress(user.getAddress(), details);
        requireString(user.getPhoneNumber(), "phoneNumber", details);
        requireString(user.getEmail(), "email", details);
        validateEmail(user.getEmail(), details);

        throwIfInvalid(details);
    }

    public void validateUpdateUser(final UpdateUserRequest user) {
        final List<ValidationError> details = new ArrayList<>();
        if (user == null) {
            details.add(new ValidationError("body", "request body is required", "required"));
            throwIfInvalid(details);
        }

        if (user.getName() != null && isBlank(user.getName())) {
            details.add(new ValidationError("name", "must not be blank", "invalid"));
        }
        if (user.getAddress() != null) {
            requireAddress(user.getAddress(), details);
        }
        validateEmail(user.getEmail(), details);

        throwIfInvalid(details);
    }

    public void validateAuthentication(final AuthenticationRequest authenticationRequest) {
        final List<ValidationError> details = new ArrayList<>();
        if (authenticationRequest == null) {
            details.add(new ValidationError("body", "request body is required", "required"));
            throwIfInvalid(details);
        }

        requireString(authenticationRequest.getEmail(), "email", details);
        requireString(authenticationRequest.getPhoneNumber(), "phoneNumber", details);
        validateEmail(authenticationRequest.getEmail(), details);

        throwIfInvalid(details);
    }

    private void requireAddress(final Address address, final List<ValidationError> details) {
        if (address == null) {
            details.add(new ValidationError("address", "is required", "required"));
            return;
        }

        requireString(address.getLine1(), "address.line1", details);
        requireString(address.getTown(), "address.town", details);
        requireString(address.getCounty(), "address.county", details);
        requireString(address.getPostcode(), "address.postcode", details);
    }

    private void requireString(final String value, final String field, final List<ValidationError> details) {
        if (isBlank(value)) {
            details.add(new ValidationError(field, "is required", "required"));
        }
    }

    private void validateEmail(final String email, final List<ValidationError> details) {
        if (email != null && !EMAIL_PATTERN.matcher(email).matches()) {
            details.add(new ValidationError("email", "must be a valid email address", "format"));
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
