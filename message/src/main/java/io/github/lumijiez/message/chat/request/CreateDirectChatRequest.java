package io.github.lumijiez.message.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateDirectChatRequest {
    private String name;

    @NotNull
    private UUID chatPartner;
}
