package io.github.lumijiez.message.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lumijiez.message.user.entity.User;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSelfResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String message;

    public static GetSelfResponseDTO from(User user, String message) {
        GetSelfResponseDTO response = new GetSelfResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setMessage(message);
        return response;
    }
}
