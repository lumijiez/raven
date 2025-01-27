package io.github.lumijiez.auth.controller;

import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/initiate")
    public ResponseEntity<AuthResponseDTO> initiateRegistration(@Valid @RequestBody RegisterRequestDTO request) {
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
        addHttpToken(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/login/initiate")
    public ResponseEntity<AuthResponseDTO> initiateLogin(@Valid @RequestBody LoginRequestDTO request) {
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
        addHttpToken(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }


    @PostMapping("/find")
    public ResponseEntity<UserDetailsDTO> find(@Valid @RequestBody FindUserRequestDTO request) {
        return ResponseEntity.ok(userService.findUser(request));
    }

    @GetMapping("/check-login")
    public void checkLogin(HttpServletRequest request, HttpServletResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        response.setHeader("Set-Cookie",
                "authToken=" +
                        "; Max-Age=0" +       // Cookie expiration time (1 hour)
                        "; Path=/" +             // Cookie is available throughout the entire domain
                        "; Secure" +             // Cookie is sent only over HTTPS
                        "; HttpOnly" +           // Cookie is not accessible via JavaScript
                        "; SameSite=Strict");

        response.setStatus(HttpServletResponse.SC_OK);
    }


    private void addHttpToken(HttpServletResponse response, String token) {
        response.setHeader("Set-Cookie",
                "authToken=" + token +
                        "; Max-Age=3600" +       // Cookie expiration time (1 hour)
                        "; Path=/" +             // Cookie is available throughout the entire domain
                        "; Secure" +             // Cookie is sent only over HTTPS
                        "; HttpOnly" +           // Cookie is not accessible via JavaScript
                        "; SameSite=Strict");
    }
}
