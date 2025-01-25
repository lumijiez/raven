package io.github.lumijiez.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class IncorrectCredentialsException extends AuthException {
    public IncorrectCredentialsException() {
        super("Incorrect credentials, either email or password");
    }
}