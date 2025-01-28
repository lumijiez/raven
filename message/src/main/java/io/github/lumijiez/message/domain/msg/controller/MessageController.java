package io.github.lumijiez.message.domain.msg.controller;

import io.github.lumijiez.message.broker.KafkaProducer;
import io.github.lumijiez.message.security.jwt.JwtClaims;
import io.github.lumijiez.message.domain.msg.dto.request.MessageQueryRequestDTO;
import io.github.lumijiez.message.domain.msg.dto.request.MessageSendRequestDTO;
import io.github.lumijiez.message.domain.msg.dto.response.MessageDTO;
import io.github.lumijiez.message.domain.msg.dto.response.MessageQueryResponseDTO;
import io.github.lumijiez.message.domain.msg.service.MessageService;
import io.github.lumijiez.model.kafka.KafkaMessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;
    private final KafkaProducer kafkaProducer;

    public MessageController(MessageService messageService, KafkaProducer kafkaProducer) {
        this.messageService = messageService;
        this.kafkaProducer = kafkaProducer;
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