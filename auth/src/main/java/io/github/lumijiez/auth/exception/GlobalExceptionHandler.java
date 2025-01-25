package io.github.lumijiez.auth.exception;

import io.github.lumijiez.auth.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDTO<Void> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage
                        .append(error.getDefaultMessage())
                        .append(". ")
        );

        return ApiResponseDTO.error(errorMessage.toString());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ApiResponseDTO.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiResponseDTO<Void> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ApiResponseDTO.error(ex.getMessage());
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ApiResponseDTO<Void> handlerIncorrectCredentialsException(IncorrectCredentialsException ex) {
        return ApiResponseDTO.error(ex.getMessage());
    }
}
