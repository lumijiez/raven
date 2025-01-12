package io.github.lumijiez.message.msg.dto.response;

import io.github.lumijiez.message.msg.entity.Message;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageQueryResponseDTO {
    List<Message> messageList;
}
