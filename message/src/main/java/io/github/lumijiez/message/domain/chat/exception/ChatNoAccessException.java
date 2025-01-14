package io.github.lumijiez.message.domain.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ChatNoAccessException extends ChatException {
    public ChatNoAccessException() {
        super("You don't have access to this chat");
    }
}