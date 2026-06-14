package com.example.eaglebank.facade;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.eaglebank.model.domain.Transaction;
import com.example.eaglebank.model.json.CreateTransactionRequest;
import com.example.eaglebank.model.json.TransactionResponse;
import com.example.eaglebank.service.AuthService;
import com.example.eaglebank.service.TransactionService;
import com.example.eaglebank.validation.BankAccountRequestValidator;
import com.example.eaglebank.validation.TransactionRequestValidator;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;
    private final AuthService authService;
    private final BankAccountRequestValidator bankAccountRequestValidator;
    private final TransactionRequestValidator transactionRequestValidator;

    public TransactionController(
            final TransactionService transactionService,
            final AuthService authService,
            final BankAccountRequestValidator bankAccountRequestValidator,
            final TransactionRequestValidator transactionRequestValidator) {
        this.transactionService = transactionService;
        this.authService = authService;
        this.bankAccountRequestValidator = bankAccountRequestValidator;
        this.transactionRequestValidator = transactionRequestValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @PathVariable final String accountNumber,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader,
            @RequestBody final CreateTransactionRequest request) {
        final String userId = authService.getAuthenticatedUserId(authorizationHeader);
        bankAccountRequestValidator.validateAccountNumber(accountNumber);
        transactionRequestValidator.validateCreateTransaction(request);
        return toResponse(transactionService.createTransaction(accountNumber, userId, request));
    }

    @GetMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getTransaction(
            @PathVariable final String accountNumber,
            @PathVariable final String transactionId,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader) {
        final String userId = authService.getAuthenticatedUserId(authorizationHeader);
        bankAccountRequestValidator.validateAccountNumber(accountNumber);
        transactionRequestValidator.validateTransactionId(transactionId);
        return toResponse(transactionService.getTransactionForUser(accountNumber, userId, transactionId));
    }

    private TransactionResponse toResponse(final Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getReference(),
                transaction.getUserId(),
                transaction.getCreatedTimestamp());
    }
}
