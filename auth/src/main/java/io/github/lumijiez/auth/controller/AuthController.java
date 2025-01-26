package io.github.lumijiez.auth.controller;

import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        log.info("Login request: {}", request);
        AuthResponseDTO auth = userService.authenticateUser(request);

        Cookie cookie = new Cookie("authToken", auth.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok(userService.authenticateUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/find")
    public ResponseEntity<UserDetailsDTO> find(@Valid @RequestBody FindUserRequestDTO request) {
        return ResponseEntity.ok(userService.findUser(request));
    }
}
