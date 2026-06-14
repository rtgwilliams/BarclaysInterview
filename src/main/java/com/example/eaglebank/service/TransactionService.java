package com.example.eaglebank.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.TransactionNotFoundException;
import com.example.eaglebank.model.domain.Transaction;
import com.example.eaglebank.model.json.CreateTransactionRequest;
import com.example.eaglebank.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    public TransactionService(
            final TransactionRepository transactionRepository,
            final BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
    }

    public Transaction createTransaction(
            final String accountNumber,
            final String userId,
            final CreateTransactionRequest request) {
        bankAccountService.applyTransaction(accountNumber, userId, request.getType(), request.getAmount());

        final Transaction transaction = new Transaction(
                generateTransactionId(),
                accountNumber,
                request.getAmount(),
                request.getCurrency(),
                request.getType(),
                request.getReference(),
                userId,
                Instant.now());

        transactionRepository.createTransaction(transaction);
        return transaction;
    }

    public Transaction getTransactionForUser(
            final String accountNumber,
            final String userId,
            final String transactionId) {
        bankAccountService.getAccountForUser(accountNumber, userId);
        final Transaction transaction = transactionRepository.getTransaction(accountNumber, transactionId);
        if (transaction == null) {
            throw new TransactionNotFoundException(transactionId);
        }
        return transaction;
    }

    private String generateTransactionId() {
        return "tan-" + UUID.randomUUID().toString().replace("-", "");
    }
}
