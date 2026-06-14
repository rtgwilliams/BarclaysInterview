package com.example.eaglebank.model.json;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTransactionRequest {
    private BigDecimal amount;
    private String currency;
    private String type;
    private String reference;
}
