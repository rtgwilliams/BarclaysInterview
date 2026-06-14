package com.example.eaglebank.model.json;

import com.example.eaglebank.model.domain.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    private String name;
    private Address address;
    private String phoneNumber;
    private String email;
}
