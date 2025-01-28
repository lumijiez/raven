package io.github.lumijiez.message.domain.msg.controller;

import io.github.lumijiez.message.domain.msg.service.ECCryptographyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    private final ECCryptographyService cryptographyService;

    public CryptoController(ECCryptographyService cryptographyService) {
        this.cryptographyService = cryptographyService;
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String message) throws Exception {
        byte[] encrypted = cryptographyService.encrypt(message);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String encryptedBase64) throws Exception {
        byte[] encrypted = Base64.getDecoder().decode(encryptedBase64);
        return cryptographyService.decrypt(encrypted);
    }
}