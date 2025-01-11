package io.github.lumijiez.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindUserRequestDTO {
    @NotBlank
    private String usernameOrEmail;
}

