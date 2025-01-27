package io.github.lumijiez.message.broker;

import io.github.lumijiez.message.domain.msg.dto.response.MessageDTO;
import io.github.lumijiez.message.domain.msg.service.MessageService;
import io.github.lumijiez.model.kafka.KafkaMessage;
import io.github.lumijiez.model.kafka.KafkaMessageResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class KafkaConsumer {

    private final MessageService messageService;
    private final KafkaProducer producer;

    public KafkaConsumer(KafkaProducer producer, MessageService messageService) {
        this.producer = producer;
        this.messageService = messageService;
    }

    @KafkaListener(topics = "chat.requests", groupId = "chat-consumers")
    public void listenRequests(KafkaMessage kafkaMessage) {
        System.out.println("Received request: " + kafkaMessage);
        MessageDTO msg = messageService.sendMessageTrusty(kafkaMessage);
        producer.sendToClient(new KafkaMessageResponse(msg.getId(), msg.getSender(), msg.getChatId(), LocalDateTime.ofInstant(msg.getTimestamp(), ZoneId.systemDefault()), msg.getContent()));
    }
}