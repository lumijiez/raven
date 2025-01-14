package io.github.lumijiez.message.domain.chat.dto.response;

import lombok.Data;

@Data
public class ChatCreateResponseDTO {
    private ChatResponseDTO chat;

    public static ChatCreateResponseDTO from(ChatResponseDTO chat) {
        ChatCreateResponseDTO response = new ChatCreateResponseDTO();
        response.setChat(chat);
        return response;
    }
}
