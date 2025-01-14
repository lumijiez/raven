package io.github.lumijiez.message.domain.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChatNotFoundException extends ChatException {
    public ChatNotFoundException() {
        super("User not found");
    }
}