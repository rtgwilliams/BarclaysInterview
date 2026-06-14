package com.example.eaglebank.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.eaglebank.model.domain.Transaction;

@Repository
public class TransactionRepository {
    private final Map<String, Transaction> transactionDatabase = new ConcurrentHashMap<>();

    public void createTransaction(final Transaction transaction) {
        transactionDatabase.put(transaction.getId(), transaction);
    }

    public Transaction getTransaction(final String accountNumber, final String transactionId) {
        final Transaction transaction = transactionDatabase.get(transactionId);
        if (transaction == null || !transaction.getAccountNumber().equals(accountNumber)) {
            return null;
        }
        return transaction;
    }
}
