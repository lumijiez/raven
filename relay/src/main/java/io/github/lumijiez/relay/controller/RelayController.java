package io.github.lumijiez.relay.controller;

import io.github.lumijiez.relay.dto.ChatMessage;
import io.github.lumijiez.relay.security.jwt.JwtClaims;
import io.github.lumijiez.relay.service.RelayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@Slf4j
public class RelayController {

    private final RelayService relayService;
    private final SimpMessagingTemplate messagingTemplate;

    public RelayController(RelayService relayService, SimpMessagingTemplate messagingTemplate) {
        this.relayService = relayService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message, Principal principal) {
        if (principal == null) {
            log.warn("Received message with null principal");
            return;
        }

        if (!(principal instanceof JwtClaims claims)) {
            log.warn("Received message with invalid principal type: {}", principal.getClass());
            return;
        }

        message.setSenderUsername(claims.getUsername());
        message.setSenderId(claims.getSub());
        message.setTimestamp(LocalDateTime.now());

        ChatMessage processedMessage = relayService.processMessage(message);
        messagingTemplate.convertAndSend("/topic/" + message.getChatId(), processedMessage);
    }
}
