package io.github.lumijiez.message.domain.msg.dto.response;

import io.github.lumijiez.message.domain.msg.entity.Message;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageQueryResponseDTO {

    @SuppressWarnings("unused")
    List<Message> messageList;
}
