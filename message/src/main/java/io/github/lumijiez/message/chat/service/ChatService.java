package io.github.lumijiez.message.chat.service;

import io.github.lumijiez.message.chat.dto.request.CreateDirectChatRequestDTO;
import io.github.lumijiez.message.chat.dto.response.ChatCreateResponseDTO;
import io.github.lumijiez.message.chat.dto.response.ChatQueryResponseDTO;
import io.github.lumijiez.message.chat.dto.response.ChatResponseDTO;
import io.github.lumijiez.message.chat.entity.Chat;
import io.github.lumijiez.message.chat.exception.ChatAlreadyExistsException;
import io.github.lumijiez.message.chat.repository.ChatRepository;
import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.exception.UserNotFoundException;
import io.github.lumijiez.message.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public ChatService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public ChatQueryResponseDTO getUserChats(JwtClaims claims) {
        List<Chat> chats = chatRepository.findByParticipantsContains(claims.getSub());
        return ChatQueryResponseDTO.from(chats);
    }

    @Transactional
    public ChatCreateResponseDTO createDirectChat(JwtClaims claims, CreateDirectChatRequestDTO request) {
        UUID creatorId = claims.getSub();
        UUID chatPartner = request.getChatPartner();

        if (!userService.exists(creatorId)) throw new UserNotFoundException(creatorId.toString());
        if (!userService.exists(chatPartner)) throw new UserNotFoundException(chatPartner.toString());

        if (chatRepository.findByParticipantsContains(creatorId).stream()
                .filter(chat -> !chat.isGroupChat())
                .anyMatch(chat -> chat.getParticipants().size() == 2 &&
                        chat.getParticipants().contains(chatPartner)))
            throw new ChatAlreadyExistsException();

        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());
        chat.setChatName(request.getName());
        chat.setParticipants(List.of(creatorId, chatPartner));
        chat.setGroupChat(false);
        Chat savedChat = chatRepository.save(chat);

        return ChatCreateResponseDTO.from(ChatResponseDTO.from(savedChat));
    }

    @Transactional(readOnly = true)
    public boolean hasAccessToChat(UUID userId, UUID chatId) {
        return chatRepository.findById(chatId)
                .map(chat -> chat.getParticipants().contains(userId))
                .orElse(false);
    }
}
