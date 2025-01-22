package io.github.lumijiez.message.broker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    UUID senderId;
    String senderUsername;
    LocalDateTime timestamp;
    UUID chatId;
    String content;
}