package io.github.lumijiez.message.msg.request;

import lombok.Data;

@Data
public class MessageSendRequest {
    private String chatId;
    private String content;
}
