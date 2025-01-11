package io.github.lumijiez.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String username) {
        super(String.format("User not found by: %s", username));
    }
}