package com.example.eaglebank.model.domain;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {
    private final String id;
    private final String accountNumber;
    private final BigDecimal amount;
    private final String currency;
    private final String type;
    private final String reference;
    private final String userId;
    private final Instant createdTimestamp;
}
