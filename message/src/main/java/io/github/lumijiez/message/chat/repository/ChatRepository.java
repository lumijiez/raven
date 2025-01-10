package io.github.lumijiez.message.chat.repository;

import io.github.lumijiez.message.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByMembersContains(UUID member);
}
