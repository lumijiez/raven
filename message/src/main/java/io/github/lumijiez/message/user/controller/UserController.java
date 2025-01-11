package io.github.lumijiez.message.user.controller;

import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.user.response.GetUserResponse;
import io.github.lumijiez.message.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<GetUserResponse> getSelf(Authentication auth) {
        return userService.get((JwtClaims) auth.getDetails());
    }
}