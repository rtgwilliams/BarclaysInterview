package com.example.eaglebank.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.eaglebank.model.domain.ProcessedUser;

@Repository
public class UserRepository {

    private final Map<String, ProcessedUser> userDatabase = new ConcurrentHashMap<>();

    public void createUser(final ProcessedUser user) {
        userDatabase.put(user.getId(), user);
    }

    public ProcessedUser getUser(final String userId) {
        return userDatabase.get(userId);
    }

    public ProcessedUser findByEmail(final String email) {
        return userDatabase.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public void deleteUser(final String userId) {
        userDatabase.remove(userId);
    }

    public ProcessedUser updateUser(final ProcessedUser user) {
        userDatabase.put(user.getId(), user);
        return user;
    }
}
