package io.github.lumijiez.message.domain.chat.exception;

public abstract class ChatException extends RuntimeException {
    protected ChatException(String message) {
        super(message);
    }
}
