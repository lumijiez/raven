package io.github.lumijiez.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginInitiateDTO {
    private String email;
    private String password;
}
