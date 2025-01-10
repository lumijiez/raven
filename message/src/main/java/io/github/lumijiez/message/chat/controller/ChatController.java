package io.github.lumijiez.message.chat.controller;

import io.github.lumijiez.message.chat.request.ChatCreateRequest;
import io.github.lumijiez.message.chat.request.ChatInviteRequest;
import io.github.lumijiez.message.chat.response.ChatCreateResponse;
import io.github.lumijiez.message.chat.response.ChatInviteResponse;
import io.github.lumijiez.message.chat.response.ChatQueryResponse;
import io.github.lumijiez.message.chat.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ChatQueryResponse> getUserChats() {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(chatService.getUserChats(sub));
    }

    @PostMapping("/create")
    public ResponseEntity<ChatCreateResponse> createChat(@Valid @RequestBody ChatCreateRequest request) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(chatService.createChat(sub, request));
    }
    @PostMapping("/addUser")
    public ResponseEntity<ChatInviteResponse> addUser(@Valid @RequestBody ChatInviteRequest request) {
        return ResponseEntity.ok(chatService.addUserToChat(request.getChatId(), request.getUserId()));
    }
}
