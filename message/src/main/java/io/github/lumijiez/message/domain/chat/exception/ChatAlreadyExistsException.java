package io.github.lumijiez.message.domain.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChatAlreadyExistsException extends ChatException {
    public ChatAlreadyExistsException() {
        super("Chat with this user already exists");
    }
}
