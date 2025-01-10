package io.github.lumijiez.message.chat.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "chats")
public class Chat {

    @Id
    private ObjectId id;
    private String name;
    private List<UUID> members;
}
