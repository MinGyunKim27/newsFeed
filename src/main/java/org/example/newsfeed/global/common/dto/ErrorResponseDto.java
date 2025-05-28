package org.example.newsfeed.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;

    public ErrorResponseDto(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.name();
        this.message = message;
    }
}
