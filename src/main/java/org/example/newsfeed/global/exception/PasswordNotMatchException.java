package org.example.newsfeed.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PasswordNotMatchException extends RuntimeException{

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordNotMatchException() {
        super(MESSAGE);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}

