package io.github.lumijiez.auth.controller;

import io.github.lumijiez.auth.dto.request.*;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.service.TwoFactorAuthService;
import io.github.lumijiez.auth.service.UserService;
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
    private final TwoFactorAuthService twoFactorAuthService;

    public AuthController(UserService userService, TwoFactorAuthService twoFactorAuthService) {
        this.userService = userService;
        this.twoFactorAuthService = twoFactorAuthService;
    }

    @PostMapping("/register/initiate")
    public ResponseEntity<String> registerInitiate(@RequestBody RegisterInitiateDTO request) {
        twoFactorAuthService.registerInitiate(request);
        return ResponseEntity.ok("Verification email sent.");
    }

    @PostMapping("/register/complete")
    public ResponseEntity<String> registerComplete(@RequestBody RegisterCompleteDTO request, HttpServletResponse response) {
        AuthResponseDTO auth = twoFactorAuthService.registerComplete(request);
        addHttpToken(response, auth.getToken());
        return ResponseEntity.ok("OK!");
    }

    @PostMapping("/login/initiate")
    public ResponseEntity<String> loginInitiate(@RequestBody LoginInitiateDTO request) {
        twoFactorAuthService.loginInitiate(request);
        return ResponseEntity.ok("If the credentials are correct, a verification code has been sent.");
    }

    @PostMapping("/login/complete")
    public ResponseEntity<String> loginComplete(@RequestBody LoginCompleteDTO request, HttpServletResponse response) {
        AuthResponseDTO auth = twoFactorAuthService.loginComplete(request);
        addHttpToken(response, auth.getToken());
        return ResponseEntity.ok("OK!");
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
                        "; Max-Age=0" +
                        "; Path=/" +
                        "; Secure" +
                        "; HttpOnly" +
                        "; SameSite=Strict");

        response.setStatus(HttpServletResponse.SC_OK);
    }


    private void addHttpToken(HttpServletResponse response, String token) {
        response.setHeader("Set-Cookie",
                "authToken=" + token +
                        "; Max-Age=3600" +
                        "; Path=/" +
                        "; Secure" +
                        "; HttpOnly" +
                        "; SameSite=Strict");
    }
}
