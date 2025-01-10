package io.github.lumijiez.message.chat.response;

import lombok.Data;

@Data
public class ChatCreateResponse {
    private ChatResponse chat;
    private String error;

    public static ChatCreateResponse success(ChatResponse chat) {
        ChatCreateResponse response = new ChatCreateResponse();
        response.setChat(chat);
        return response;
    }

    public static ChatCreateResponse error(String error) {
        ChatCreateResponse response = new ChatCreateResponse();
        response.setError(error);
        return response;
    }
}
