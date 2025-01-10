package io.github.lumijiez.message.msg.controller;

import io.github.lumijiez.message.msg.request.MessageQueryRequest;
import io.github.lumijiez.message.msg.request.MessageSendRequest;
import io.github.lumijiez.message.msg.response.MessageQueryResponse;
import io.github.lumijiez.message.msg.response.MessageSendResponse;
import io.github.lumijiez.message.msg.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/getMessages")
    public ResponseEntity<MessageQueryResponse> getMessages(@Valid @RequestBody MessageQueryRequest request) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(messageService.getMessages(sub, request));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<MessageSendResponse> sendMessage(@RequestBody MessageSendRequest request) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(201).body(messageService.sendMessage(sub, request));
    }
}