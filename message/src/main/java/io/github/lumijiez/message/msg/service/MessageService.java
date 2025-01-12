package io.github.lumijiez.message.msg.service;

import io.github.lumijiez.message.chat.exception.ChatNoAccessException;
import io.github.lumijiez.message.chat.service.ChatService;
import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.msg.dto.request.MessageQueryRequestDTO;
import io.github.lumijiez.message.msg.dto.request.MessageSendRequestDTO;
import io.github.lumijiez.message.msg.dto.response.MessageDTO;
import io.github.lumijiez.message.msg.dto.response.MessageQueryResponseDTO;
import io.github.lumijiez.message.msg.entity.Message;
import io.github.lumijiez.message.msg.repository.MessageRepository;
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
        if (!chatService.hasAccessToChat(claims.getSub(), request.getChatId())) {
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
        if (!chatService.hasAccessToChat(claims.getSub(), request.getChatId())) {
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
