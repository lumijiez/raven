package io.github.lumijiez.message.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateDirectChatRequestDTO {
    private String name;

    @NotNull
    private UUID chatPartner;
}
