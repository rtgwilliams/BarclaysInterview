package com.example.eaglebank.model.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBankAccountRequest {
    private String name;
    private String accountType;
}
