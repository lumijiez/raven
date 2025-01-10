package io.github.lumijiez.message.msg.response;

import lombok.Data;

import java.util.List;

@Data
public class MessageQueryResponse {
    List<MessageResponse> messageList;
    String error;
}
