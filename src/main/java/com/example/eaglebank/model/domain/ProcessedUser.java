package com.example.eaglebank.model.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessedUser {
    private final UUID userId;
    private final String userName;
}
