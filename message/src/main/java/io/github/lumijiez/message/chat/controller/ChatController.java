package io.github.lumijiez.message.chat.controller;

import io.github.lumijiez.message.chat.request.CreateDirectChatRequest;
import io.github.lumijiez.message.chat.request.CreateGroupChatRequest;
import io.github.lumijiez.message.chat.response.ChatCreateResponse;
import io.github.lumijiez.message.chat.response.ChatQueryResponse;
import io.github.lumijiez.message.chat.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

//    @GetMapping("/getAll")
//    public ResponseEntity<ChatQueryResponse> getUserChats() {
//        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(chatService.getUserChats(sub));
//    }

//    @PostMapping("/createDirect")
//    public ResponseEntity<ChatCreateResponse> createChat(@Valid @RequestBody CreateDirectChatRequest request) {
//        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(chatService.createChat(sub, request));
//    }

//    @PostMapping("/createGroup")
//    public ResponseEntity<ChatCreateResponse> createGroup(@Valid @RequestBody CreateGroupChatRequest request) {
//        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(chatService.createChat(sub, request));
//    }
}
