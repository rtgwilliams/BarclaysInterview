package com.example.eaglebank.model.json;

import java.time.Instant;

import com.example.eaglebank.model.domain.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private final String id;
    private final String name;
    private final Address address;
    private final String phoneNumber;
    private final String email;
    private final Instant createdTimestamp;
    private final Instant updatedTimestamp;
}
