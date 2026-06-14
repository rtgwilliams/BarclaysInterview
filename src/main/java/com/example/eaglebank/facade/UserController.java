package com.example.eaglebank.facade;

import org.springframework.web.bind.annotation.RestController;

import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.domain.UserUpdate;
import com.example.eaglebank.model.json.CreateUserRequest;
import com.example.eaglebank.model.json.UpdateUserRequest;
import com.example.eaglebank.model.json.UserResponse;
import com.example.eaglebank.service.UserService;
import com.example.eaglebank.transformer.UserTransformer;
import com.example.eaglebank.validation.UserRequestValidator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer;
    private final UserRequestValidator userRequestValidator;

    public UserController() {
        this.userService = new UserService();
        this.userTransformer = new UserTransformer();
        this.userRequestValidator = new UserRequestValidator();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody final CreateUserRequest user) {
        userRequestValidator.validateCreateUser(user);
        final NewUser newUser = userTransformer.toNewUser(user);
        final ProcessedUser createdUser = userService.createUser(newUser);

        return userTransformer.toUserResponse(createdUser);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable final String userId) {
        userRequestValidator.validateUserId(userId);
        final ProcessedUser processedUser = userService.getUser(userId);
        return userTransformer.toUserResponse(processedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final String userId) {
        userRequestValidator.validateUserId(userId);
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable final String userId, @RequestBody final UpdateUserRequest user) {
        userRequestValidator.validateUserId(userId);
        userRequestValidator.validateUpdateUser(user);
        final UserUpdate userUpdate = userTransformer.toUserUpdate(user);
        final ProcessedUser updatedUser = userService.updateUser(userId, userUpdate);
        return userTransformer.toUserResponse(updatedUser);
    }
}
