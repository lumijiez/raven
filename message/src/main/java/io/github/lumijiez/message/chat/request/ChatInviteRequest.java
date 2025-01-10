package io.github.lumijiez.message.chat.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatInviteRequest {
    private UUID userId;
    private String chatId;
}
