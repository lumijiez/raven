package io.github.lumijiez.relay.service;

import io.github.lumijiez.model.kafka.KafkaMessage;
import io.github.lumijiez.relay.broker.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RelayService {

    private final KafkaProducer producer;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RelayService(KafkaProducer producer, SimpMessagingTemplate simpMessagingTemplate) {
        this.producer = producer;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void processMessage(KafkaMessage kafkaMessage) {
        producer.sendToServer(kafkaMessage);
        log.info("Processing kafkaMessage: senderId={}, chatId='{}', content={}",
                kafkaMessage.getSenderId(),
                kafkaMessage.getChatId(),
                kafkaMessage.getContent());
    }
}
