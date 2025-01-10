package io.github.lumijiez.message.user.response;

import io.github.lumijiez.message.user.entity.User;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Data
public class UserResponse {
    private ObjectId id;
    private UUID userId;
    private List<String> chats;
    private String error;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUserId(user.getUserId());
        response.setChats(user.getChats());
        return response;
    }
}
