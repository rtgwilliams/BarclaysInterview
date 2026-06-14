package com.example.eaglebank.facade;

import org.springframework.web.bind.annotation.RestController;

import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.json.CreateUserRequest;
import com.example.eaglebank.model.json.UpdateUserRequest;
import com.example.eaglebank.model.json.UserResponse;
import com.example.eaglebank.service.AuthService;
import com.example.eaglebank.service.UserService;
import com.example.eaglebank.validation.UserRequestValidator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRequestValidator userRequestValidator;
    private final AuthService authService;

    public UserController(
            final UserService userService,
            final UserRequestValidator userRequestValidator,
            final AuthService authService) {
        this.userService = userService;
        this.userRequestValidator = userRequestValidator;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody final CreateUserRequest user) {
        userRequestValidator.validateCreateUser(user);
        return toResponse(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(
            @PathVariable final String userId,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader) {
        userRequestValidator.validateUserId(userId);
        authService.requireUserAccess(authorizationHeader, userId);
        final ProcessedUser processedUser = userService.getUser(userId);
        return toResponse(processedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable final String userId,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader) {
        userRequestValidator.validateUserId(userId);
        authService.requireUserAccess(authorizationHeader, userId);
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(
            @PathVariable final String userId,
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader,
            @RequestBody final UpdateUserRequest user) {
        userRequestValidator.validateUserId(userId);
        authService.requireUserAccess(authorizationHeader, userId);
        userRequestValidator.validateUpdateUser(user);
        return toResponse(userService.updateUser(userId, user));
    }

    private UserResponse toResponse(final ProcessedUser processedUser) {
        return new UserResponse(
                processedUser.getId(),
                processedUser.getName(),
                processedUser.getAddress(),
                processedUser.getPhoneNumber(),
                processedUser.getEmail(),
                processedUser.getCreatedTimestamp(),
                processedUser.getUpdatedTimestamp());
    }
}
