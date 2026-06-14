package com.example.eaglebank.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.UserNotFoundException;
import com.example.eaglebank.exception.BadRequestException;
import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.domain.UserUpdate;
import com.example.eaglebank.model.json.BadRequestErrorResponse.ValidationError;
import com.example.eaglebank.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProcessedUser createUser(final NewUser newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            throw new BadRequestException(
                    "Invalid details supplied",
                    List.of(new ValidationError("email", "already belongs to a user", "duplicate")));
        }

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
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    public ProcessedUser findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(final String userId) {
        getUser(userId);
        userRepository.deleteUser(userId);
    }

    public ProcessedUser updateUser(final String userId, final UserUpdate userUpdate) {
        final ProcessedUser existingUser = getUser(userId);
        if (userUpdate.getEmail() != null && !userUpdate.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            final ProcessedUser userWithEmail = userRepository.findByEmail(userUpdate.getEmail());
            if (userWithEmail != null) {
                throw new BadRequestException(
                        "Invalid details supplied",
                        List.of(new ValidationError("email", "already belongs to a user", "duplicate")));
            }
        }

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
