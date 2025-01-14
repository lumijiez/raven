package io.github.lumijiez.message.domain.chat.repository;

import io.github.lumijiez.message.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantsContains(UUID member);
    Optional<Chat> findById(UUID id);
}
