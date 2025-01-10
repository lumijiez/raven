package io.github.lumijiez.message.chat.service;

import io.github.lumijiez.message.chat.entity.Chat;
import io.github.lumijiez.message.chat.repository.ChatRepository;
import io.github.lumijiez.message.chat.request.ChatCreateRequest;
import io.github.lumijiez.message.chat.request.ChatInviteRequest;
import io.github.lumijiez.message.chat.response.*;
import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public ChatQueryResponse getUserChats(String sub) {
        UUID userId = UUID.fromString(sub);
        List<Chat> chats = chatRepository.findByMembersContains(userId);

        ChatQueryResponse response = new ChatQueryResponse();
        response.setChats(chats.stream().map(ChatResponse::from).toList());
        return response;
    }

    @Transactional
    public ChatCreateResponse createChat(String sub, ChatCreateRequest request) {
        UUID creatorId = UUID.fromString(sub);

        List<UUID> members = new ArrayList<>(request.getMembers());
        members.add(creatorId);

        if (userRepository.existsAllByUserIdIn(members)) {
            return ChatCreateResponse.error("One or more users don't exist");
        }

        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setMembers(members);

        Chat savedChat = chatRepository.save(chat);
        String chatId = savedChat.getId().toString();

        List<User> users = userRepository.findAllByUserIdIn(members);
        for (User user : users) {
            if (user.getChats() == null) {
                user.setChats(new ArrayList<>());
            }
            user.getChats().add(chatId);
            userRepository.save(user);
        }

        return ChatCreateResponse.success(ChatResponse.from(savedChat));
    }

    @Transactional
    public ChatInviteResponse addUserToChat(String chatId, UUID userId) {
        ChatInviteResponse response = new ChatInviteResponse();

        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            response.setStatus("Chat not found");
            return response;
        }

        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            response.setStatus("User not found");
            return response;
        }

        if (chat.getMembers().contains(userId)) {
            response.setStatus("User is already in chat");
            return response;
        }

        chat.getMembers().add(userId);
        chatRepository.save(chat);

        if (user.getChats() == null) {
            user.setChats(new ArrayList<>());
        }

        user.getChats().add(chatId);
        userRepository.save(user);

        response.setStatus("Success");
        return response;
    }
}
