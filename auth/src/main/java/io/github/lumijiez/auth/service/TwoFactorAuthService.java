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
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String subject = "RAVEN - Verify Your Account";
        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7fc; margin: 0; padding: 0; }"
                + ".email-container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; }"
                + ".email-header { background-color: #5c6bc0; color: white; padding: 20px; text-align: center; }"
                + ".email-body { padding: 20px; color: #333; line-height: 1.6; }"
                + ".email-body h2 { color: #5c6bc0; font-size: 24px; margin-bottom: 20px; }"
                + ".email-body p { font-size: 16px; margin-bottom: 10px; }"
                + ".verification-code { background-color: #5c6bc0; color: white; padding: 15px; font-size: 22px; font-weight: bold; text-align: center; margin: 20px 0; border-radius: 8px; }"
                + ".email-footer { text-align: center; padding: 20px; font-size: 14px; color: #888; border-top: 1px solid #ddd; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='email-container'>"
                + "<div class='email-header'>"
                + "<h1>RAVEN</h1>"
                + "</div>"
                + "<div class='email-body'>"
                + "<h2>Account Verification</h2>"
                + "<p>Dear user,</p>"
                + "<p>We received a request to verify your account. Please use the following verification code to complete the process:</p>"
                + "<div class='verification-code'>" + verificationCode + "</div>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Thank you for choosing RAVEN!</p>"
                + "</div>"
                + "<div class='email-footer'>"
                + "<p>Best regards,<br>RAVEN</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail(recipientEmail, subject, content);
    }

    public void sendLoginVerificationEmail(String recipientEmail, String verificationCode) throws MessagingException {
        String subject = "RAVEN - Login Verification Code";
        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7fc; margin: 0; padding: 0; }"
                + ".email-container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; }"
                + ".email-header { background-color: #ff7043; color: white; padding: 20px; text-align: center; }"
                + ".email-body { padding: 20px; color: #333; line-height: 1.6; }"
                + ".email-body h2 { color: #ff7043; font-size: 24px; margin-bottom: 20px; }"
                + ".email-body p { font-size: 16px; margin-bottom: 10px; }"
                + ".verification-code { background-color: #ff7043; color: white; padding: 15px; font-size: 22px; font-weight: bold; text-align: center; margin: 20px 0; border-radius: 8px; }"
                + ".email-footer { text-align: center; padding: 20px; font-size: 14px; color: #888; border-top: 1px solid #ddd; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='email-container'>"
                + "<div class='email-header'>"
                + "<h1>RAVEN</h1>"
                + "</div>"
                + "<div class='email-body'>"
                + "<h2>Login Verification</h2>"
                + "<p>Dear user,</p>"
                + "<p>We received a login request for your account. Please use the following verification code to complete the login:</p>"
                + "<div class='verification-code'>" + verificationCode + "</div>"
                + "<p>If you did not attempt to log in, please ignore this email.</p>"
                + "<p>Thank you for your attention!</p>"
                + "</div>"
                + "<div class='email-footer'>"
                + "<p>Best regards,<br>RAVEN</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>"
                ;

        sendEmail(recipientEmail, subject, content);
    }

    private void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }
}

