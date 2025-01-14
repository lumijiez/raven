package io.github.lumijiez.message.msg.exception;

import io.github.lumijiez.message.common.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class MessageExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDTO<Void> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage
                        .append(error.getDefaultMessage())
                        .append(". "));

        return ApiResponseDTO.failure(errorMessage.toString());
    }
}
