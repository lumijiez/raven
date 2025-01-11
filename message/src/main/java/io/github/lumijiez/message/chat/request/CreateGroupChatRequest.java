package io.github.lumijiez.message.chat.request;

import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateGroupChatRequest {
    private String name;

    private List<UUID> participants;
}