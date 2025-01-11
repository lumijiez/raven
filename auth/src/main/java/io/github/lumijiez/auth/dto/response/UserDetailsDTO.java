package io.github.lumijiez.auth.dto.response;


import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailsDTO {
    private UUID id;
    private String username;
    private String email;
}
