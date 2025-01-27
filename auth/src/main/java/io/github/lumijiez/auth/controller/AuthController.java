package io.github.lumijiez.auth.controller;

import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.ErrorResponse;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    private static final String COOKIE_NAME = "authToken";

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/initiate")
    public ResponseEntity<AuthResponseDTO> initiateRegistration(
            @Valid @RequestBody RegisterRequestDTO request) throws AuthException {
        log.info("Registration initiation request for email: {}", request.getEmail());
        return ResponseEntity.ok(userService.initiateRegistration(request));
    }

    @PostMapping("/register/complete")
    public ResponseEntity<AuthResponseDTO> completeRegistration(
            @RequestParam String email,
            @RequestParam String code,
            HttpServletResponse response) {
        log.info("Registration completion request for email: {}", email);
        AuthResponseDTO auth = userService.completeRegistration(email, code);
        addAuthCookie(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/login/initiate")
    public ResponseEntity<AuthResponseDTO> initiateLogin(
            @Valid @RequestBody LoginRequestDTO request) throws AuthException {
        log.info("Login initiation request for: {}", request.getUsernameOrEmail());
        return ResponseEntity.ok(userService.initiateLogin(request));
    }

    @PostMapping("/login/complete")
    public ResponseEntity<AuthResponseDTO> completeLogin(
            @RequestParam String email,
            @RequestParam String code,
            HttpServletResponse response) {
        log.info("Login completion request for email: {}", email);
        AuthResponseDTO auth = userService.completeLogin(email, code);
        addAuthCookie(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/check")
    public ResponseEntity<Void> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        removeAuthCookie(response);
        return ResponseEntity.ok().build();
    }

    private void addAuthCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void removeAuthCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
