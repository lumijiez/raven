package io.github.lumijiez.auth.service;

import io.github.lumijiez.auth.domain.entity.TempLogin;
import io.github.lumijiez.auth.domain.entity.TempUser;
import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.LoginCompleteDTO;
import io.github.lumijiez.auth.dto.request.LoginInitiateDTO;
import io.github.lumijiez.auth.dto.request.RegisterCompleteDTO;
import io.github.lumijiez.auth.dto.request.RegisterInitiateDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.exception.AuthException;
import io.github.lumijiez.auth.exception.IncorrectCredentialsException;
import io.github.lumijiez.auth.exception.UserAlreadyExistsException;
import io.github.lumijiez.auth.exception.UserNotFoundException;
import io.github.lumijiez.auth.repository.TempLoginRepository;
import io.github.lumijiez.auth.repository.TempUserRepository;
import io.github.lumijiez.auth.repository.UserRepository;
import io.github.lumijiez.auth.security.JwtHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TwoFactorAuthService {

    private final TempUserRepository tempUserRepository;
    private final TempLoginRepository tempLoginRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final JavaMailSender mailSender;

    public TwoFactorAuthService(
            TempUserRepository tempUserRepository,
            TempLoginRepository tempLoginRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtHelper jwtHelper,
            JavaMailSender mailSender) {
        this.tempUserRepository = tempUserRepository;
        this.tempLoginRepository = tempLoginRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.mailSender = mailSender;
    }

    public void registerInitiate(RegisterInitiateDTO request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("Username", "already taken");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException("Email", "already taken");

        String verificationCode = generateVerificationCode();

        TempUser tempUser = TempUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .verificationCode(verificationCode)
                .createdAt(LocalDateTime.now())
                .build();

        tempUserRepository.save(tempUser);
        sendEmail(request.getEmail(), "Verify your account", "Your verification code is: " + verificationCode);
    }

    public AuthResponseDTO registerComplete(RegisterCompleteDTO request) {
        TempUser tempUser = tempUserRepository.findByEmailAndVerificationCode(request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new AuthException("Core incorrect"));

        User user = User.builder()
                .username(tempUser.getUsername())
                .email(tempUser.getEmail())
                .password(tempUser.getPassword())  // Already encrypted
                .build();

        userRepository.save(user);
        tempUserRepository.delete(tempUser);  // Cleanup temp data

        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }

    public void loginInitiate(LoginInitiateDTO request) {
        User user = userRepository.findByUsernameOrEmail(request.getEmail(), request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new IncorrectCredentialsException();

        String verificationCode = generateVerificationCode();

        TempLogin tempLogin = TempLogin.builder()
                .email(user.getEmail())
                .verificationCode(verificationCode)
                .createdAt(LocalDateTime.now())
                .build();

        tempLoginRepository.save(tempLogin);
        sendEmail(user.getEmail(), "Login Verification Code", "Your login code is: " + verificationCode);
    }

    public AuthResponseDTO loginComplete(LoginCompleteDTO request) {
        TempLogin tempLogin = tempLoginRepository.findByEmailAndVerificationCode(request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new AuthException("Code not correct"));

        User user = userRepository.findByUsernameOrEmail(request.getEmail(), request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        tempLoginRepository.delete(tempLogin);

        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

