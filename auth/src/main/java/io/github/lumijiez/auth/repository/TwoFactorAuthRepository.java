package io.github.lumijiez.auth.repository;

import io.github.lumijiez.auth.domain.entity.TwoFactorAuth;
import io.github.lumijiez.auth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, UUID> {
    Optional<TwoFactorAuth> findByEmailAndCodeAndUsedFalseAndRegistrationTrue(String email, String code);
    Optional<TwoFactorAuth> findByEmailAndCodeAndUsedFalseAndRegistrationFalse(String email, String code);

    @Modifying
    @Query("UPDATE TwoFactorAuth t SET t.used = true WHERE t.email = :email AND t.registration = true AND t.used = false")
    void invalidateExistingRegistrationCodes(@Param("email") String email);

    @Scheduled(cron = "0 */15 * * * *")
    @Modifying
    @Query("DELETE FROM TwoFactorAuth t WHERE t.expiryTime < CURRENT_TIMESTAMP OR t.used = true")
    void cleanupExpiredCodes();
}