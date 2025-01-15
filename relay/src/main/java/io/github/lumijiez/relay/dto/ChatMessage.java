package io.github.lumijiez.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private UUID chatId;
    private String content;
    private String senderUsername;
    private UUID senderId;
    private LocalDateTime timestamp;
}
