package org.example.newsfeed.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNameDuplicatesException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 존재하는 사용자 이름입니다.";

    public UserNameDuplicatesException() {
        super(MESSAGE);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
