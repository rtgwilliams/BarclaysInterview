package com.example.eaglebank.transformer;

import org.springframework.stereotype.Component;

import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.domain.UserUpdate;
import com.example.eaglebank.model.json.CreateUserRequest;
import com.example.eaglebank.model.json.UpdateUserRequest;
import com.example.eaglebank.model.json.UserResponse;

@Component
public class UserTransformer {

    public UserTransformer() {
    }

    public NewUser toNewUser(final CreateUserRequest user) {
        return new NewUser(user.getName(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
    }

    public UserResponse toUserResponse(final ProcessedUser processedUser) {
        return new UserResponse(
                processedUser.getId(),
                processedUser.getName(),
                processedUser.getAddress(),
                processedUser.getPhoneNumber(),
                processedUser.getEmail(),
                processedUser.getCreatedTimestamp(),
                processedUser.getUpdatedTimestamp());
    }

    public UserUpdate toUserUpdate(final UpdateUserRequest user) {
        return new UserUpdate(user.getName(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
    }
}
