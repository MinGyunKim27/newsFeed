package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends BaseException {

    public PasswordNotMatchException() {
        super(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
    }
}
