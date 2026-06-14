package com.example.eaglebank.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.BankAccountNotFoundException;
import com.example.eaglebank.exception.ForbiddenException;
import com.example.eaglebank.exception.InsufficientFundsException;
import com.example.eaglebank.model.domain.BankAccount;
import com.example.eaglebank.model.json.CreateBankAccountRequest;
import com.example.eaglebank.repository.BankAccountRepository;

@Service
public class BankAccountService {
    private static final String SORT_CODE = "10-10-10";
    private static final String CURRENCY = "GBP";
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    private final AtomicInteger accountNumberSequence = new AtomicInteger(1);
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(final BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createAccount(final String userId, final CreateBankAccountRequest request) {
        final Instant now = Instant.now();
        final BankAccount bankAccount = new BankAccount(
                generateAccountNumber(),
                SORT_CODE,
                request.getName(),
                request.getAccountType(),
                ZERO,
                CURRENCY,
                userId,
                now,
                now);

        bankAccountRepository.createAccount(bankAccount);
        return bankAccount;
    }

    public BankAccount getAccountForUser(final String accountNumber, final String userId) {
        final BankAccount bankAccount = bankAccountRepository.getAccount(accountNumber);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException(accountNumber);
        }
        if (!bankAccount.getUserId().equals(userId)) {
            throw new ForbiddenException("The user is not allowed to access the bank account details");
        }
        return bankAccount;
    }

    public BankAccount applyTransaction(
            final String accountNumber,
            final String userId,
            final String transactionType,
            final BigDecimal amount) {
        final BankAccount existingAccount = getAccountForUser(accountNumber, userId);
        final BigDecimal newBalance = calculateNewBalance(existingAccount.getBalance(), transactionType, amount);
        final BankAccount updatedAccount = new BankAccount(
                existingAccount.getAccountNumber(),
                existingAccount.getSortCode(),
                existingAccount.getName(),
                existingAccount.getAccountType(),
                newBalance,
                existingAccount.getCurrency(),
                existingAccount.getUserId(),
                existingAccount.getCreatedTimestamp(),
                Instant.now());

        return bankAccountRepository.updateAccount(updatedAccount);
    }

    private BigDecimal calculateNewBalance(
            final BigDecimal currentBalance,
            final String transactionType,
            final BigDecimal amount) {
        if ("deposit".equals(transactionType)) {
            return currentBalance.add(amount).setScale(2);
        }

        final BigDecimal newBalance = currentBalance.subtract(amount).setScale(2);
        if (newBalance.compareTo(ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        return newBalance;
    }

    private String generateAccountNumber() {
        return String.format("01%06d", accountNumberSequence.getAndIncrement());
    }
}
