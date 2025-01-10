package io.github.lumijiez.message.msg.repository;

import io.github.lumijiez.message.msg.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findTop100ByChatIdOrderByTimestampDesc(String chatId);
    List<Message> findTop100ByChatIdAndTimestampAfterOrderByTimestampDesc(String chatId, Instant lastMessageTimestamp);
}
