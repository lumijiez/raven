package io.github.lumijiez.message.msg.request;

import lombok.Data;

@Data
public class MessageQueryRequest {
    private String chatId;
    private String lastMessage;
}
