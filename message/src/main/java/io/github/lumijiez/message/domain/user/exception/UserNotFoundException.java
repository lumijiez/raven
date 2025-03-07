package io.github.lumijiez.message.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String id) {
        super("User with id " + id + " not found");
    }
}