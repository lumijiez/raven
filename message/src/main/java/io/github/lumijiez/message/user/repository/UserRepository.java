package io.github.lumijiez.message.user.repository;

import io.github.lumijiez.message.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserId(UUID userId);
    boolean existsAllByUserIdIn(List<UUID> userIds);
    List<User> findAllByUserIdIn(List<UUID> userIds);
}
