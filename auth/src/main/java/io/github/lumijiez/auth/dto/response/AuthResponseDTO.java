package io.github.lumijiez.auth.dto.response;

import lombok.Data;

package io.github.lumijiez.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String error;
    private boolean requiresTwoFactor;
    private String twoFactorId;

    public static AuthResponseDTO from(String token) {
        return AuthResponseDTO.builder()
                .token(token)
                .requiresTwoFactor(false)
                .build();
    }

    public static AuthResponseDTO requiresTwoFactor(String twoFactorId) {
        return AuthResponseDTO.builder()
                .requiresTwoFactor(true)
                .twoFactorId(twoFactorId)
                .build();
    }

    public static AuthResponseDTO error(String error) {
        return AuthResponseDTO.builder()
                .error(error)
                .build();
    }
}