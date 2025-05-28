package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class EmailDuplicatesException extends BaseException {

    public EmailDuplicatesException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
    }
}
