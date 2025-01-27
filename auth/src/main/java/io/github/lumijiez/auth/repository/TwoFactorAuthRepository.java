package io.github.lumijiez.auth.repository;

import io.github.lumijiez.auth.domain.entity.TwoFactorAuth;
import io.github.lumijiez.auth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, UUID> {
    Optional<TwoFactorAuth> findByEmailAndCodeAndUsedFalseAndRegistrationTrue(String email, String code);
    Optional<TwoFactorAuth> findByEmailAndCodeAndUsedFalseAndRegistrationFalse(String email, String code);
}