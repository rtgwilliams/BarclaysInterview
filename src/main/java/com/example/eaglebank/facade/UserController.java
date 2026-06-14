package com.example.eaglebank.facade;

import org.springframework.web.bind.annotation.RestController;

import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.json.User;
import com.example.eaglebank.service.UserService;
import com.example.eaglebank.transformer.UserTransformer;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer;

    public UserController() {
        this.userService = new UserService();
        this.userTransformer = new UserTransformer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody final User user) {
        final NewUser newUser = userTransformer.toNewUser(user);
        final ProcessedUser createdUser = userService.createUser(newUser);

        return userTransformer.toUser(createdUser);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable final UUID userId) {
        final ProcessedUser processedUser = userService.getUser(userId);
        return userTransformer.toUser(processedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final UUID userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable final UUID userId, @RequestBody final User user) {
        final ProcessedUser processedUser = userTransformer.toProcessedUser(user);
        final ProcessedUser updatedUser = userService.updateUser(processedUser);
        return userTransformer.toUser(updatedUser);
    }
}
