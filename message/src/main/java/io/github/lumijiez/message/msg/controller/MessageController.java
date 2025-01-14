package io.github.lumijiez.message.msg.controller;

import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.msg.dto.request.MessageQueryRequestDTO;
import io.github.lumijiez.message.msg.dto.request.MessageSendRequestDTO;
import io.github.lumijiez.message.msg.dto.response.MessageDTO;
import io.github.lumijiez.message.msg.dto.response.MessageQueryResponseDTO;
import io.github.lumijiez.message.msg.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/get")
    public ResponseEntity<MessageQueryResponseDTO> getMessages(
            @Valid @RequestBody MessageQueryRequestDTO request,
            Authentication auth) {
        return ResponseEntity.ok(messageService.getMessages((JwtClaims) auth.getDetails(), request));
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(
            @Valid @RequestBody MessageSendRequestDTO request,
            Authentication auth) {
        MessageDTO response = messageService.sendMessage((JwtClaims) auth.getDetails(), request);
        return ResponseEntity.ok(response);
    }
}