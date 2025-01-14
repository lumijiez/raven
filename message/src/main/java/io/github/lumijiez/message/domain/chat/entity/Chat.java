package io.github.lumijiez.message.domain.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "chats")
public class Chat {

    @Id
    private UUID id;
    private String chatName;
    private boolean isGroupChat;
    private List<UUID> participants;
}
