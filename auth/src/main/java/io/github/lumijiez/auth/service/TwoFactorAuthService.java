package io.github.lumijiez.auth.service;

import io.github.lumijiez.auth.domain.entity.TwoFactorAuth;
import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.repository.TwoFactorAuthRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class TwoFactorAuthService {
    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final EmailService emailService;

    public TwoFactorAuthService(TwoFactorAuthRepository twoFactorAuthRepository, EmailService emailService) {
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.emailService = emailService;
    }

    public void generateAndSendCode(User user) {
        String code = generateCode();
        TwoFactorAuth twoFactorAuth = TwoFactorAuth.builder()
                .user(user)
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        twoFactorAuthRepository.save(twoFactorAuth);
        emailService.sendTwoFactorCode(user.getEmail(), code);
    }

    public boolean validateCode(User user, String code) {
        return twoFactorAuthRepository.findByUserAndCodeAndUsedFalse(user, code)
                .filter(tfa -> !tfa.isUsed() && LocalDateTime.now().isBefore(tfa.getExpiryTime()))
                .map(tfa -> {
                    tfa.setUsed(true);
                    twoFactorAuthRepository.save(tfa);
                    return true;
                })
                .orElse(false);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
