package io.github.lumijiez.auth.service;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationCode(String to, String code) throws AuthException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Complete Your Registration");
        message.setText("Your registration verification code is: " + code +
                "\nThis code will expire in 15 minutes." +
                "\nIf you didn't request this code, please ignore this email.");
        try {
            mailSender.send(message);
            log.info("Registration verification code sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send registration code to: {}", to, e);
            throw new AuthException("Failed to send registration code");
        }
    }

    public void sendLoginCode(String to, String code) throws AuthException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Login Verification Code");
        message.setText("Your login verification code is: " + code +
                "\nThis code will expire in 5 minutes." +
                "\nIf you didn't request this code, please contact support immediately.");
        try {
            mailSender.send(message);
            log.info("Login verification code sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send login code to: {}", to, e);
            throw new AuthException("Failed to send login code");
        }
    }
}
