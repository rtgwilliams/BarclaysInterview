package com.example.eaglebank.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.eaglebank.exception.UnauthorizedException;

@Service
public class JwtService {
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");
    private static final long TOKEN_TTL_SECONDS = 60 * 60 * 24;

    private final String secret;

    public JwtService(@Value("${eaglebank.jwt.secret:local-development-secret}") final String secret) {
        this.secret = secret;
    }

    public String generateToken(final String userId) {
        final long issuedAt = Instant.now().getEpochSecond();
        final long expiresAt = issuedAt + TOKEN_TTL_SECONDS;

        final String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        final String payload = base64Url("{\"sub\":\"" + userId + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiresAt + "}");
        final String unsignedToken = header + "." + payload;

        return unsignedToken + "." + sign(unsignedToken);
    }

    public String validateTokenAndGetSubject(final String token) {
        try {
            final String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new UnauthorizedException("Access token is missing or invalid");
            }

            final String unsignedToken = parts[0] + "." + parts[1];
            final String expectedSignature = sign(unsignedToken);
            if (!MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                throw new UnauthorizedException("Access token is missing or invalid");
            }

            final String payload = decodeBase64Url(parts[1]);
            final long expiresAt = extractExpiry(payload);
            if (expiresAt < Instant.now().getEpochSecond()) {
                throw new UnauthorizedException("Access token is missing or invalid");
            }

            return extractSubject(payload);
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Access token is missing or invalid");
        }
    }

    private String extractSubject(final String payload) {
        final Matcher matcher = SUBJECT_PATTERN.matcher(payload);
        if (!matcher.find()) {
            throw new UnauthorizedException("Access token is missing or invalid");
        }
        return matcher.group(1);
    }

    private long extractExpiry(final String payload) {
        final Matcher matcher = EXPIRY_PATTERN.matcher(payload);
        if (!matcher.find()) {
            throw new UnauthorizedException("Access token is missing or invalid");
        }
        return Long.parseLong(matcher.group(1));
    }

    private String sign(final String value) {
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("JWT signing is not available", exception);
        }
    }

    private String base64Url(final String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeBase64Url(final String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
