package com.example.eaglebank.model.json;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankAccountResponse {
    private final String accountNumber;
    private final String sortCode;
    private final String name;
    private final String accountType;
    private final BigDecimal balance;
    private final String currency;
    private final Instant createdTimestamp;
    private final Instant updatedTimestamp;
}
