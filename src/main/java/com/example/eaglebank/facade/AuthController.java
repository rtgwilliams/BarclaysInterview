package com.example.eaglebank.facade;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.eaglebank.model.json.AuthenticationRequest;
import com.example.eaglebank.model.json.AuthenticationResponse;
import com.example.eaglebank.service.AuthService;
import com.example.eaglebank.validation.UserRequestValidator;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    
    private final AuthService authService;
    private final UserRequestValidator userRequestValidator;

    public AuthController(final AuthService authService, final UserRequestValidator userRequestValidator) {
        this.authService = authService;
        this.userRequestValidator = userRequestValidator;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {
        userRequestValidator.validateAuthentication(authenticationRequest);
        final String token = authService.authenticate(
                authenticationRequest.getEmail(),
                authenticationRequest.getPhoneNumber());

        return new AuthenticationResponse(token, "Bearer");
    }
}
