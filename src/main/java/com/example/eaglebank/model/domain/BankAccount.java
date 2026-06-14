package com.example.eaglebank.model.domain;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankAccount {
    private final String accountNumber;
    private final String sortCode;
    private final String name;
    private final String accountType;
    private final BigDecimal balance;
    private final String currency;
    private final String userId;
    private final Instant createdTimestamp;
    private final Instant updatedTimestamp;
}
