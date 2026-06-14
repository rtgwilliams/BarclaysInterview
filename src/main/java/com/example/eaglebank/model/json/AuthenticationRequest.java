package com.example.eaglebank.model.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {
    private final String email;
    private final String phoneNumber;
}
