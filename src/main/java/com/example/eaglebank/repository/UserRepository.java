package com.example.eaglebank.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.example.eaglebank.model.domain.ProcessedUser;

public class UserRepository {

    private Map<UUID, ProcessedUser> userDatabase = new ConcurrentHashMap<>();

    public void createUser(final ProcessedUser user) {
        userDatabase.put(user.getUserId(), user);
    }

    public ProcessedUser getUser(final UUID userId) {
        return userDatabase.get(userId);
    }

    public void deleteUser(final UUID userId) {
        userDatabase.remove(userId);
    }

    public ProcessedUser updateUser(final ProcessedUser user) {
        userDatabase.put(user.getUserId(), user);
        return user;
    }
}
