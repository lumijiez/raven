package io.github.lumijiez.relay.broker;

import io.github.lumijiez.model.kafka.KafkaMessageResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public KafkaConsumer(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @KafkaListener(topics = "chat.responses", groupId = "chat-consumers")
    public void listenRequests(KafkaMessageResponse response) {
        simpMessagingTemplate.convertAndSend("/topic/chats/" + response.getChatId(), response);
        System.out.println("Received response: " + response);
    }
}