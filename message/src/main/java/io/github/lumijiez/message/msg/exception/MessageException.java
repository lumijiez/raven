package io.github.lumijiez.message.msg.exception;

@SuppressWarnings("unused")
public abstract class MessageException extends RuntimeException {
    protected MessageException(String message) {
        super(message);
    }
}
