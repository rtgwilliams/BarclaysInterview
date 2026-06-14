package com.example.eaglebank.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdate {
    private final String name;
    private final Address address;
    private final String phoneNumber;
    private final String email;
}
