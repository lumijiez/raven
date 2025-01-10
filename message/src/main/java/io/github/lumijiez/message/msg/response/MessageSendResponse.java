package io.github.lumijiez.message.msg.response;

import io.github.lumijiez.message.msg.entity.Message;
import lombok.Data;

@Data
public class MessageSendResponse {
    Message message;
    String error;
}
