package io.github.lumijiez.message.msg.repository;

import io.github.lumijiez.message.msg.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findTop100ByChatIdOrderByTimestampDesc(UUID chatId);
    List<Message> findTop100ByChatIdAndTimestampAfterOrderByTimestampDesc(UUID chatId, Instant lastMessageTimestamp);
}
