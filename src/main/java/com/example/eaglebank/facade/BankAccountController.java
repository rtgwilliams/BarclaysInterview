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

import com.example.eaglebank.model.domain.BankAccount;
import com.example.eaglebank.model.json.BankAccountResponse;
import com.example.eaglebank.model.json.CreateBankAccountRequest;
import com.example.eaglebank.service.AuthService;
import com.example.eaglebank.service.BankAccountService;
import com.example.eaglebank.validation.BankAccountRequestValidator;

@RestController
@RequestMapping("/v1/accounts")
public class BankAccountController {
    
    private final BankAccountService bankAccountService;
    private final AuthService authService;
    private final BankAccountRequestValidator bankAccountRequestValidator;

    public BankAccountController(
            final BankAccountService bankAccountService,
            final AuthService authService,
            final BankAccountRequestValidator bankAccountRequestValidator) {
        this.bankAccountService = bankAccountService;
        this.authService = authService;
        this.bankAccountRequestValidator = bankAccountRequestValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponse createAccount(
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader,
            @RequestBody final CreateBankAccountRequest request) {
        final String userId = authService.getAuthenticatedUserId(authorizationHeader);
        bankAccountRequestValidator.validateCreateAccount(request);
        return toResponse(bankAccountService.createAccount(userId, request));
    }

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public BankAccountResponse getAccount(
            @PathVariable final String accountNumber,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader) {
        final String userId = authService.getAuthenticatedUserId(authorizationHeader);
        bankAccountRequestValidator.validateAccountNumber(accountNumber);
        return toResponse(bankAccountService.getAccountForUser(accountNumber, userId));
    }

    private BankAccountResponse toResponse(final BankAccount bankAccount) {
        return new BankAccountResponse(
                bankAccount.getAccountNumber(),
                bankAccount.getSortCode(),
                bankAccount.getName(),
                bankAccount.getAccountType(),
                bankAccount.getBalance(),
                bankAccount.getCurrency(),
                bankAccount.getCreatedTimestamp(),
                bankAccount.getUpdatedTimestamp());
    }
}
