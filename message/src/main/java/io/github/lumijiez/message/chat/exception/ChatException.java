package io.github.lumijiez.message.chat.exception;

public abstract class ChatException extends RuntimeException {
    protected ChatException(String message) {
        super(message);
    }
}
