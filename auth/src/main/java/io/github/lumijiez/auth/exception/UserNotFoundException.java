package io.github.lumijiez.auth.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String username) {
        super(String.format("User not found by: %s", username));
    }
}