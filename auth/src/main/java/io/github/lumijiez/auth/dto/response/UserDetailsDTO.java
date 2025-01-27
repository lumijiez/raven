package io.github.lumijiez.auth.dto.response;


import io.github.lumijiez.auth.domain.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailsDTO {
    private UUID id;
    private String username;
    private String email;

    public static UserDetailsDTO from(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
