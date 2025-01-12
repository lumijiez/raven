package io.github.lumijiez.message.chat.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@SuppressWarnings("unused")
public class CreateGroupChatRequestDTO {
    private String name;

    private List<UUID> participants;
}