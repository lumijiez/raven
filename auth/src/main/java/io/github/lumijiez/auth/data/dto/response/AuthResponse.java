package io.github.lumijiez.auth.data.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String error;

    public AuthResponse(String token) {
        this.token = token;
    }
}
