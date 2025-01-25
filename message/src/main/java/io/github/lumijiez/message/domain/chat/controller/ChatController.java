package io.github.lumijiez.message.domain.chat.controller;

import io.github.lumijiez.message.domain.chat.dto.request.CreateDirectChatRequestDTO;
import io.github.lumijiez.message.domain.chat.dto.response.ChatCreateResponseDTO;
import io.github.lumijiez.message.domain.chat.dto.response.ChatQueryResponseDTO;
import io.github.lumijiez.message.domain.chat.service.ChatService;
import io.github.lumijiez.message.security.jwt.JwtClaims;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<ChatQueryResponseDTO> getUserChats(Authentication auth) {
        return ResponseEntity.ok(chatService.getUserChats((JwtClaims) auth.getDetails()));
    }

    @PostMapping("/create-direct")
    public ResponseEntity<ChatCreateResponseDTO> createChat(
            @Valid @RequestBody CreateDirectChatRequestDTO request,
            Authentication auth) {
        return ResponseEntity.ok(chatService.createDirectChat((JwtClaims) auth.getDetails(), request));
    }

//    @PostMapping("/createGroup")
//    public ResponseEntity<ChatCreateResponseDTO> createGroup(@Valid @RequestBody CreateGroupChatRequestDTO request) {
//        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(chatService.createChat(sub, request));
//    }
}
