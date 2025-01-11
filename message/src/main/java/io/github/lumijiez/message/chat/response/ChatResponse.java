package io.github.lumijiez.message.chat.response;

import io.github.lumijiez.message.chat.entity.Chat;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Data
public class ChatResponse {
    private String id;
    private String name;
    private List<UUID> members;

//    public static ChatResponse from(Chat chat) {
//        ChatResponse response = new ChatResponse();
//        response.setId(chat.getId().toString());
//        response.setName(chat.getName());
//        response.setMembers(chat.getMembers());
//        return response;
//    }
}