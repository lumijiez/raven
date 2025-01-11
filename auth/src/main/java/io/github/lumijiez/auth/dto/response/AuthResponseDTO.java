package io.github.lumijiez.auth.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String error;

    public static AuthResponseDTO success(String token) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        return response;
    }

    public static AuthResponseDTO error(String error) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setError(error);
        return response;
    }
}
