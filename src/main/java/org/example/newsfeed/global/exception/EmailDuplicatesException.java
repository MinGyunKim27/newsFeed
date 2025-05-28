package org.example.newsfeed.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailDuplicatesException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public EmailDuplicatesException() {
        super(MESSAGE);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
