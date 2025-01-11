package io.github.lumijiez.message.chat.service;

import io.github.lumijiez.message.chat.entity.Chat;
import io.github.lumijiez.message.chat.repository.ChatRepository;
import io.github.lumijiez.message.chat.request.CreateDirectChatRequest;
import io.github.lumijiez.message.chat.response.*;
import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

//    public ChatQueryResponse getUserChats(String sub) {
//        UUID userId = UUID.fromString(sub);
//        List<Chat> chats = chatRepository.findByParticipantsContains(userId);
//
//        ChatQueryResponse response = new ChatQueryResponse();
//        response.setChats(chats.stream().map(ChatResponse::from).toList());
//        return response;
//    }

//    @Transactional
//    public ChatCreateResponse createDirectChat(String sub, CreateDirectChatRequest request) {
//        UUID creatorId = UUID.fromString(sub);
//        UUID chatPartner = request.getChatPartner();
//
//
//        if (userRepository.existsAllByUserIdIn(members)) {
//            return ChatCreateResponse.error("One or more users don't exist");
//        }
//
//        Chat chat = new Chat();
//        chat.setName(request.getName());
//        chat.setMembers(members);
//
//        Chat savedChat = chatRepository.save(chat);
//        String chatId = savedChat.getId().toString();
//
//        List<User> users = userRepository.findAllByUserIdIn(members);
//        for (User user : users) {
//            if (user.getChats() == null) {
//                user.setChats(new ArrayList<>());
//            }
//            user.getChats().add(chatId);
//            userRepository.save(user);
//        }
//
//        return ChatCreateResponse.success(ChatResponse.from(savedChat));
//    }
}
