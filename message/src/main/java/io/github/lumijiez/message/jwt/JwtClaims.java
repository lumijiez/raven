package io.github.lumijiez.message.jwt;

import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Data
public class JwtClaims implements Principal {
    private UUID sub;
    private String username;
    private String email;
    private String error;
    private boolean isSuccess;

    public static JwtClaims success(String sub, String username, String email) {
        JwtClaims dto = new JwtClaims();
        dto.setSub(UUID.fromString(sub));
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setSuccess(true);
        return dto;
    }

    public static JwtClaims failure(String error) {
        JwtClaims dto = new JwtClaims();
        dto.setError(error);
        dto.setSuccess(false);
        return dto;
    }

    @Override
    public String getName() {
        return sub != null ? sub.toString() : null;
    }
}
