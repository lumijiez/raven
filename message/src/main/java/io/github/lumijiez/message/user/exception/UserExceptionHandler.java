package io.github.lumijiez.message.user.exception;

import io.github.lumijiez.message.common.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponseDTO<Void> handleUserNotFoundException(UserNotFoundException ex) {
        return ApiResponseDTO.failure(ex.getMessage());
    }
}
