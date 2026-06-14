package com.example.eaglebank.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.eaglebank.model.domain.BankAccount;

@Repository
public class BankAccountRepository {
    private final Map<String, BankAccount> accountDatabase = new ConcurrentHashMap<>();

    public void createAccount(final BankAccount bankAccount) {
        accountDatabase.put(bankAccount.getAccountNumber(), bankAccount);
    }

    public BankAccount getAccount(final String accountNumber) {
        return accountDatabase.get(accountNumber);
    }

    public boolean existsByUserId(final String userId) {
        return accountDatabase.values().stream()
                .anyMatch(bankAccount -> bankAccount.getUserId().equals(userId));
    }

    public BankAccount updateAccount(final BankAccount bankAccount) {
        accountDatabase.put(bankAccount.getAccountNumber(), bankAccount);
        return bankAccount;
    }
}
