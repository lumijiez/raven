package io.github.lumijiez.auth.repository;

import io.github.lumijiez.auth.domain.entity.TempLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TempLoginRepository extends JpaRepository<TempLogin, UUID> {
    Optional<TempLogin> findByEmailAndVerificationCode(String email, String code);
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
