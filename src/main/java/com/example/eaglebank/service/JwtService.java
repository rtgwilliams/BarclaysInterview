package com.example.eaglebank.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.ApiException;

@Service
public class JwtService {
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");

    public String generateToken(final String userId) {
        final String header = base64Url("{\"alg\":\"none\",\"typ\":\"JWT\"}");
        final String payload = base64Url("{\"sub\":\"" + userId + "\"}");
        return header + "." + payload + ".";
    }

    public String validateTokenAndGetSubject(final String token) {
        try {
            final String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw invalidToken();
            }

            return SUBJECT_PATTERN.matcher(decodeBase64Url(parts[1]))
                    .results()
                    .findFirst()
                    .map(match -> match.group(1))
                    .orElseThrow(this::invalidToken);
        } catch (IllegalArgumentException exception) {
            throw invalidToken();
        }
    }

    private ApiException invalidToken() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
    }

    private String base64Url(final String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeBase64Url(final String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
