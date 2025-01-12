package io.github.lumijiez.message.msg.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private UUID id;
    private UUID sender;
    private UUID chatId;
    private Instant timestamp;
    private String content;
}
