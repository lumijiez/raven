package io.github.lumijiez.message.domain.msg.dto.request;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class MessageSendRequestDTO {

    @NonNull
    private UUID chatId;

    @NonNull
    private String content;
}
