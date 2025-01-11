package io.github.lumijiez.auth.controller;

import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
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
