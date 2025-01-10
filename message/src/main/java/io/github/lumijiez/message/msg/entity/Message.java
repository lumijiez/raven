package io.github.lumijiez.message.msg.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private ObjectId id;
    private UUID sender;
    private String chatId;
    private Instant timestamp;
    private String content;
}
