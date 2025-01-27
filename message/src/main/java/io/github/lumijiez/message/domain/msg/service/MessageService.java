package io.github.lumijiez.message.domain.msg.service;

import io.github.lumijiez.message.domain.chat.exception.ChatNoAccessException;
import io.github.lumijiez.message.domain.chat.service.ChatService;
import io.github.lumijiez.message.security.jwt.JwtClaims;
import io.github.lumijiez.message.domain.msg.dto.request.MessageQueryRequestDTO;
import io.github.lumijiez.message.domain.msg.dto.request.MessageSendRequestDTO;
import io.github.lumijiez.message.domain.msg.dto.response.MessageDTO;
import io.github.lumijiez.message.domain.msg.dto.response.MessageQueryResponseDTO;
import io.github.lumijiez.message.domain.msg.entity.Message;
import io.github.lumijiez.message.domain.msg.repository.MessageRepository;
import io.github.lumijiez.model.kafka.KafkaMessage;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
    private final ChatService chatService;
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
    }

    @Transactional(readOnly = true)
    public MessageQueryResponseDTO getMessages(JwtClaims claims, MessageQueryRequestDTO request) {
        if (chatService.lacksAccessToChat(claims.getSub(), request.getChatId())) {
            throw new ChatNoAccessException();
        }

        List<Message> messages;

        if (request.getLastMessage() != null) {
            Message lastMessage = messageRepository.findById(request.getLastMessage().toString())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Last message not found with id: %s", request.getLastMessage())
                    ));

            messages = messageRepository.findTop100ByChatIdAndTimestampAfterOrderByTimestampDesc(
                    request.getChatId(),
                    lastMessage.getTimestamp()
            );
        } else {
            messages = messageRepository.findTop100ByChatIdOrderByTimestampDesc(request.getChatId());
        }

        return MessageQueryResponseDTO.builder()
                .messageList(messages)
                .build();
    }

    @Transactional
    public MessageDTO sendMessage(JwtClaims claims, MessageSendRequestDTO request) {
        if (chatService.lacksAccessToChat(claims.getSub(), request.getChatId())) {
            throw new AccessDeniedException("User does not have access to this chat");
        }

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setSender(claims.getSub());
        message.setTimestamp(Instant.now());
        message.setChatId(request.getChatId());
        message.setContent(request.getContent());

        return MessageDTO.from(messageRepository.save(message));
    }

    @Transactional
    public MessageDTO sendMessageTrusty(KafkaMessage message) {
        if (chatService.lacksAccessToChat(claims.getSub(), request.getChatId())) {
            throw new AccessDeniedException("User does not have access to this chat");
        }

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setSender(claims.getSub());
        message.setTimestamp(Instant.now());
        message.setChatId(request.getChatId());
        message.setContent(request.getContent());

        return MessageDTO.from(messageRepository.save(message));
    }
}
