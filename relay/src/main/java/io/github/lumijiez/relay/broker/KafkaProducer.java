package io.github.lumijiez.relay.broker;

import io.github.lumijiez.model.kafka.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToServer(KafkaMessage kafkaMessage) {
        kafkaTemplate.send("chat.requests", kafkaMessage);
        log.info("KafkaMessage sent to requests : {}", kafkaMessage);
    }
}