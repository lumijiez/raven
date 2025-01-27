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
import jakarta.mail.MessagingException;
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

    public void registerInitiate(RegisterInitiateDTO request) throws MessagingException {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("Username", request.getUsername());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException("Email", request.getEmail());

        String verificationCode = generateVerificationCode();

        TempUser tempUser = TempUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .verificationCode(verificationCode)
                .createdAt(LocalDateTime.now())
                .build();

        tempUserRepository.save(tempUser);
        sendVerificationEmail(request.getEmail(), verificationCode);
    }

    public AuthResponseDTO registerComplete(RegisterCompleteDTO request) {
        TempUser tempUser = tempUserRepository.findByEmailAndVerificationCode(request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new AuthException("Core incorrect"));

        User user = User.builder()
                .username(tempUser.getUsername())
                .email(tempUser.getEmail())
                .password(tempUser.getPassword())
                .build();

        userRepository.save(user);
        tempUserRepository.delete(tempUser);

        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }

    public void loginInitiate(LoginInitiateDTO request) throws MessagingException {
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
        sendLoginVerificationEmail(user.getEmail(), verificationCode);
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

    public void sendVerificationEmail(String recipientEmail, String verificationCode) throws MessagingException {
        String subject = "Verify Your Account";
        String content = "<html>"
                + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<h2 style='color: #5c6bc0;'>Account Verification</h2>"
                + "<p>Dear user,</p>"
                + "<p>We received a request to verify your account. Please use the following verification code:</p>"
                + "<h3 style='background-color: #5c6bc0; color: white; padding: 10px;'>"
                + verificationCode
                + "</h3>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Thank you for choosing us!</p>"
                + "<br><p>Best regards, <br> Your Company Team</p>"
                + "</body>"
                + "</html>";

        sendEmail(recipientEmail, subject, content);
    }

    public void sendLoginVerificationEmail(String recipientEmail, String verificationCode) throws MessagingException {
        String subject = "Login Verification Code";
        String content = "<html>"
                + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<h2 style='color: #ff7043;'>Login Verification</h2>"
                + "<p>Dear user,</p>"
                + "<p>We received a login request for your account. Use the following verification code to complete the login:</p>"
                + "<h3 style='background-color: #ff7043; color: white; padding: 10px;'>"
                + verificationCode
                + "</h3>"
                + "<p>If you did not attempt to log in, please ignore this email.</p>"
                + "<p>Thank you for your attention!</p>"
                + "<br><p>Best regards, <br> Your Company Team</p>"
                + "</body>"
                + "</html>";

        sendEmail(recipientEmail, subject, content);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

