package io.github.lumijiez.message.user.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;
    private UUID userId;
    private List<String> chats;
}