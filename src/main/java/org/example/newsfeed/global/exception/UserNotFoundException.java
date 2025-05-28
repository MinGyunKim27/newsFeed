package org.example.newsfeed.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
