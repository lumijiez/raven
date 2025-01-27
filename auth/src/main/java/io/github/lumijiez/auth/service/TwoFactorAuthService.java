package io.github.lumijiez.auth.service;

import io.github.lumijiez.auth.domain.entity.TwoFactorAuth;
import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.repository.TwoFactorAuthRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TwoFactorAuthService {
    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    public TwoFactorAuthService(TwoFactorAuthRepository twoFactorAuthRepository,
                                EmailService emailService,
                                BCryptPasswordEncoder passwordEncoder) {
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void generateAndSendRegistrationCode(RegisterRequestDTO request) {
        String code = generateCode();
        TwoFactorAuth twoFactorAuth = TwoFactorAuth.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .registration(true)
                .build();

        twoFactorAuthRepository.save(twoFactorAuth);
        emailService.sendRegistrationCode(request.getEmail(), code);
    }

    public void generateAndSendLoginCode(User user) {
        String code = generateCode();
        TwoFactorAuth twoFactorAuth = TwoFactorAuth.builder()
                .email(user.getEmail())
                .code(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .registration(false)
                .build();

        twoFactorAuthRepository.save(twoFactorAuth);
        emailService.sendLoginCode(user.getEmail(), code);
    }

    public Optional<TwoFactorAuth> validateRegistrationCode(String email, String code) {
        return twoFactorAuthRepository.findByEmailAndCodeAndUsedFalseAndRegistrationTrue(email, code)
                .filter(tfa -> LocalDateTime.now().isBefore(tfa.getExpiryTime()));
    }

    public Optional<TwoFactorAuth> validateLoginCode(String email, String code) {
        return twoFactorAuthRepository.findByEmailAndCodeAndUsedFalseAndRegistrationFalse(email, code)
                .filter(tfa -> LocalDateTime.now().isBefore(tfa.getExpiryTime()));
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
