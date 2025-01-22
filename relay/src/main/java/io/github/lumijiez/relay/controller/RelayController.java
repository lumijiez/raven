package io.github.lumijiez.relay.controller;

import io.github.lumijiez.model.kafka.KafkaMessage;
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

    public RelayController(RelayService relayService) {
        this.relayService = relayService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(KafkaMessage kafkaMessage, Principal principal) {
        if (principal == null) {
            log.warn("Received kafkaMessage with null principal");
            return;
        }

        if (!(principal instanceof JwtClaims claims)) {
            log.warn("Received kafkaMessage with invalid principal type: {}", principal.getClass());
            return;
        }

        kafkaMessage.setSenderId(claims.getSub());
        kafkaMessage.setTimestamp(LocalDateTime.now());

        relayService.processMessage(kafkaMessage);
    }
}
