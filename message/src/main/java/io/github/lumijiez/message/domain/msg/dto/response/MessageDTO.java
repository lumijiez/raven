package io.github.lumijiez.message.domain.msg.dto.response;

import io.github.lumijiez.message.domain.msg.entity.Message;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MessageDTO {
    @NonNull
    private UUID id;

    @NonNull
    private UUID sender;

    @NonNull
    private UUID chatId;

    @NonNull
    private Instant timestamp;

    @NonNull
    private String content;

    public static MessageDTO from(Message message) {
        return MessageDTO.builder()
                .id(UUID.randomUUID())
                .chatId(message.getChatId())
                .content(message.getContent())
                .sender(message.getSender())
                .timestamp(Instant.now())
                .build();
    }
}
