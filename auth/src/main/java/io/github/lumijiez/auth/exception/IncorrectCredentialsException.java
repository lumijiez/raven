package io.github.lumijiez.auth.exception;


public class IncorrectCredentialsException extends AuthException {
    public IncorrectCredentialsException() {
        super("Incorrect credentials, either email or password");
    }
}