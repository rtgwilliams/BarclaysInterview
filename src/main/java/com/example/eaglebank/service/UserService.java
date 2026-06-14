package com.example.eaglebank.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public ProcessedUser createUser(final NewUser newUser) {
        final ProcessedUser processedUser = new ProcessedUser(generateUserId(), newUser.getUserName());
        userRepository.createUser(processedUser);
        return processedUser;
    }

    public ProcessedUser getUser(final UUID userId) {
        return userRepository.getUser(userId);
    }

    public void deleteUser(final UUID userId) {
        userRepository.deleteUser(userId);
    }

    public ProcessedUser updateUser(final ProcessedUser user) {
        return userRepository.updateUser(user);
    }

    private UUID generateUserId() {
        return UUID.randomUUID();
    }
}
