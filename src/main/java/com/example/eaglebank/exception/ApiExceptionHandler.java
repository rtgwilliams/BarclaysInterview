package com.example.eaglebank.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.eaglebank.model.json.BadRequestErrorResponse;
import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;
import com.example.eaglebank.model.json.ErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleBadRequest(final BadRequestException exception) {
        return new BadRequestErrorResponse(exception.getMessage(), exception.getDetails());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleUnreadableBody() {
        return new BadRequestErrorResponse(
                "Invalid details supplied",
                List.of(new ValidationError("body", "request body is invalid", "invalid")));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedError() {
        return new ErrorResponse("An unexpected error occurred");
    }
}
