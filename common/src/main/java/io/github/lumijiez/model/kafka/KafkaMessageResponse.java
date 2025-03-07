package io.github.lumijiez.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageResponse {
    UUID id;
    UUID sender;
    UUID chatId;
    LocalDateTime timestamp;
    String content;
}