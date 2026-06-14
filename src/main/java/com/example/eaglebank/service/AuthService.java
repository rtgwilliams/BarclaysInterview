package com.example.eaglebank.service;

import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.ForbiddenException;
import com.example.eaglebank.exception.UnauthorizedException;
import com.example.eaglebank.model.domain.ProcessedUser;

@Service
public class AuthService {
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(final UserService userService, final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public String authenticate(final String email, final String phoneNumber) {
        final ProcessedUser user = userService.findUserByEmail(email);
        if (user == null || !user.getPhoneNumber().equals(phoneNumber)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId());
    }

    public void requireUserAccess(final String authorizationHeader, final String requestedUserId) {
        final String authenticatedUserId = getAuthenticatedUserId(authorizationHeader);
        if (!authenticatedUserId.equals(requestedUserId)) {
            throw new ForbiddenException("The user is not allowed to access this resource");
        }
    }

    public String getAuthenticatedUserId(final String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Access token is missing or invalid");
        }

        final String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            throw new UnauthorizedException("Access token is missing or invalid");
        }

        return jwtService.validateTokenAndGetSubject(token);
    }
}
