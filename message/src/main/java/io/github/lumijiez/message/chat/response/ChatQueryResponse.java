package io.github.lumijiez.message.chat.response;

import lombok.Data;

import java.util.List;

@Data
public class ChatQueryResponse {
    private List<ChatResponse> chats;
    private String error;
}