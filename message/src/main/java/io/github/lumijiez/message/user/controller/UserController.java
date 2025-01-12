package io.github.lumijiez.message.user.controller;

import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.user.dto.response.GetSelfResponseDTO;
import io.github.lumijiez.message.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/self")
    public ResponseEntity<GetSelfResponseDTO> getSelf(Authentication auth) {
        return ResponseEntity.ok(userService.get((JwtClaims) auth.getDetails()));
    }
}