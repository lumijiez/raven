package io.github.lumijiez.message.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lumijiez.message.user.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse {
    private boolean isSuccess;

    private UUID id;
    private String username;
    private String email;
    private List<UUID> userChats;
    private String message;

    public static ResponseEntity<GetUserResponse> success(User user, String message) {
        GetUserResponse response = new GetUserResponse();
        response.setSuccess(true);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setUserChats(user.getUserChats());
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<GetUserResponse> failure(String error) {
        GetUserResponse response = new GetUserResponse();
        response.setSuccess(false);
        response.setMessage(error);
        return ResponseEntity.badRequest().body(response);
    }
}
