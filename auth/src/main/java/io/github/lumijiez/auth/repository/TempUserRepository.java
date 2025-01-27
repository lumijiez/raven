package io.github.lumijiez.auth.repository;

import io.github.lumijiez.auth.domain.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TempUserRepository extends JpaRepository<TempUser, UUID> {
    Optional<TempUser> findByVerificationCode(String code);
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
