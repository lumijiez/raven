package io.github.lumijiez.auth.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String error;

    public static AuthResponseDTO from(String token) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        return response;
    }
}
