package io.github.lumijiez.message.chat.dto.response;

import io.github.lumijiez.message.chat.entity.Chat;
import lombok.Data;

import java.util.List;

@Data
public class ChatQueryResponseDTO {
    private List<ChatResponseDTO> chats;

    public static ChatQueryResponseDTO from(List<Chat> chats) {
        ChatQueryResponseDTO response = new ChatQueryResponseDTO();
        response.setChats(chats.stream().map(ChatResponseDTO::from).toList());
        return response;
    }
}