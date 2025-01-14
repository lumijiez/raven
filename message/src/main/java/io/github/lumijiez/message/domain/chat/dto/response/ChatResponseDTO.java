package io.github.lumijiez.message.domain.chat.dto.response;

import io.github.lumijiez.message.domain.chat.entity.Chat;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatResponseDTO {
    private String id;
    private String name;
    private List<UUID> members;

    public static ChatResponseDTO from(Chat chat) {
        ChatResponseDTO response = new ChatResponseDTO();
        response.setId(chat.getId().toString());
        response.setName(chat.getChatName());
        response.setMembers(chat.getParticipants());
        return response;
    }
}