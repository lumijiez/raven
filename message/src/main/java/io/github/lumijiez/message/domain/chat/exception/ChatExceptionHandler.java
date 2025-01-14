package io.github.lumijiez.message.domain.chat.exception;

import io.github.lumijiez.message.common.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ChatExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDTO<Void> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage
                        .append(error.getDefaultMessage())
                        .append(". "));

        return ApiResponseDTO.failure(errorMessage.toString());
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ApiResponseDTO<Void> handleChatNotFoundException(ChatNotFoundException ex) {
        return ApiResponseDTO.failure(ex.getMessage());
    }

    @ExceptionHandler(ChatAlreadyExistsException.class)
    public ApiResponseDTO<Void> handleChatAlreadyExistsException(ChatAlreadyExistsException ex) {
        return ApiResponseDTO.failure(ex.getMessage());
    }

    @ExceptionHandler(ChatNoAccessException.class)
    public ApiResponseDTO<Void> handleChatNoAccessException(ChatNoAccessException ex) {
        return ApiResponseDTO.failure(ex.getMessage());
    }
}
