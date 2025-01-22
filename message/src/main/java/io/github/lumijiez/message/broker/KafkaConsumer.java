package io.github.lumijiez.message.broker;

import io.github.lumijiez.model.kafka.KafkaMessage;
import io.github.lumijiez.model.kafka.KafkaMessageResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final KafkaProducer producer;

    public KafkaConsumer(KafkaProducer producer) {
        this.producer = producer;
    }

    @KafkaListener(topics = "chat.requests", groupId = "chat-consumers")
    public void listenRequests(KafkaMessage kafkaMessage) {
        System.out.println("Received request: " + kafkaMessage);
        producer.sendToClient(new KafkaMessageResponse(kafkaMessage.getSenderId(), kafkaMessage.getChatId(), kafkaMessage.getTimestamp(), kafkaMessage.getContent()));
    }
}