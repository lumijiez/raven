package io.github.lumijiez.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTwoFactorCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your 2FA Code");
        message.setText("Your verification code is: " + code + "\nThis code will expire in 5 minutes.");
        mailSender.send(message);
    }
}
