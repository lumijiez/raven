package io.github.lumijiez.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
