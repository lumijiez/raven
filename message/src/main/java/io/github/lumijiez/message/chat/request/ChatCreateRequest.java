package io.github.lumijiez.message.chat.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatCreateRequest {
    private String name;
    private List<UUID> members;
}
