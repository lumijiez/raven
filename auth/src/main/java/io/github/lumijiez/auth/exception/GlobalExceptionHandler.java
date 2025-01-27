package io.github.lumijiez.auth.exception;

import io.github.lumijiez.auth.dto.response.ApiResponseDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage
                        .append(error.getDefaultMessage())
                        .append(". ")
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponseDTO.error(errorMessage.toString()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIncorrectCredentialsException(IncorrectCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAuthException(AuthException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }
}
