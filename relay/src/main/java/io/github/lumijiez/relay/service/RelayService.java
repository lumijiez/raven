package io.github.lumijiez.relay.service;

import io.github.lumijiez.relay.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RelayService {

    public ChatMessage processMessage(ChatMessage message) {
        log.info("Processing message: chatId={}, content='{}', sender={} ({})",
                message.getChatId(),
                message.getContent(),
                message.getSenderUsername(),
                message.getSenderId());

        return message;
    }
}
