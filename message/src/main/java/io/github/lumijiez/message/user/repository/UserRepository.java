package io.github.lumijiez.message.user.repository;

import io.github.lumijiez.message.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(UUID id);
    boolean existsById(UUID id);
}
