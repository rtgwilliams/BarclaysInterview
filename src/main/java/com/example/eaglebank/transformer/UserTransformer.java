package com.example.eaglebank.transformer;

import org.springframework.stereotype.Component;

import com.example.eaglebank.model.domain.NewUser;
import com.example.eaglebank.model.domain.ProcessedUser;
import com.example.eaglebank.model.json.User;

@Component
public class UserTransformer {

    public UserTransformer() {
    }

    public NewUser toNewUser(final User user) {
        return new NewUser(user.getUserName());
    }

    public User toUser(ProcessedUser processedUser) {
        return new User(processedUser.getUserId(), processedUser.getUserName());
    }

    public ProcessedUser toProcessedUser(final User user) {
        return new ProcessedUser(user.getUserId(), user.getUserName());
    }
}
