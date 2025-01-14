package io.github.lumijiez.message.domain.msg.dto.request;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class MessageQueryRequestDTO {

    @NonNull
    private UUID chatId;

    private UUID lastMessage;
}
