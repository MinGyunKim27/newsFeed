package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class UserNameDuplicatesException extends BaseException {

    public UserNameDuplicatesException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다.");
    }
}
