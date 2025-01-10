package io.github.lumijiez.message.msg.response;

import io.github.lumijiez.message.msg.entity.Message;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageResponse {
    private String id;
    private UUID sender;
    private String chatId;
    private Instant timestamp;
    private String content;

    public static MessageResponse from(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId().toString());
        response.setSender(message.getSender());
        response.setChatId(message.getChatId());
        response.setTimestamp(message.getTimestamp());
        response.setContent(message.getContent());
        return response;
    }
}
