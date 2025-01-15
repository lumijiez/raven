package io.github.lumijiez.message.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @KafkaListener(topicPattern = "chat.*", groupId = "chat-consumers")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}