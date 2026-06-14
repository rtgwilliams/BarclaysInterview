package com.example.eaglebank.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.ApiException;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.json.CreateUserRequest;
import com.example.eaglebank.model.json.UpdateUserRequest;
import com.example.eaglebank.repository.BankAccountRepository;
import com.example.eaglebank.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public UserService(final UserRepository userRepository, final BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public ProcessedUser createUser(final CreateUserRequest newUser) {
        final Instant now = Instant.now();
        final ProcessedUser processedUser = new ProcessedUser(
                generateUserId(),
                newUser.getName(),
                newUser.getAddress(),
                newUser.getPhoneNumber(),
                newUser.getEmail(),
                now,
                now);
        userRepository.createUser(processedUser);
        return processedUser;
    }

    public ProcessedUser getUser(final String userId) {
        final ProcessedUser user = userRepository.getUser(userId);
        if (user == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User was not found: " + userId);
        }
        return user;
    }

    public ProcessedUser findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(final String userId) {
        getUser(userId);
        if (bankAccountRepository.existsByUserId(userId)) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "A user cannot be deleted when they are associated with a bank account");
        }
        userRepository.deleteUser(userId);
    }

    public ProcessedUser updateUser(final String userId, final UpdateUserRequest userUpdate) {
        final ProcessedUser existingUser = getUser(userId);
        final ProcessedUser updatedUser = new ProcessedUser(
                existingUser.getId(),
                userUpdate.getName() == null ? existingUser.getName() : userUpdate.getName(),
                userUpdate.getAddress() == null ? existingUser.getAddress() : userUpdate.getAddress(),
                userUpdate.getPhoneNumber() == null ? existingUser.getPhoneNumber() : userUpdate.getPhoneNumber(),
                userUpdate.getEmail() == null ? existingUser.getEmail() : userUpdate.getEmail(),
                existingUser.getCreatedTimestamp(),
                Instant.now());

        return userRepository.updateUser(updatedUser);
    }

    private String generateUserId() {
        return "usr-" + UUID.randomUUID().toString().replace("-", "");
    }
}
