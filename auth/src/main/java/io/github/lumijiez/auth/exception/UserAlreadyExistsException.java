package io.github.lumijiez.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String field, String value) {
        super(String.format("User already exists with %s: %s", field, value));
    }
}