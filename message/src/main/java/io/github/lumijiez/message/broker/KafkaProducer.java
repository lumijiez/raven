package io.github.lumijiez.message.broker;

import io.github.lumijiez.model.kafka.KafkaMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, KafkaMessageResponse> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, KafkaMessageResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToClient(KafkaMessageResponse message) {
        message.setContent("aaa PROCESSED");
        kafkaTemplate.send("chat.responses", message);
        log.info("KafkaMessage sent to client : {}", message);
    }
}