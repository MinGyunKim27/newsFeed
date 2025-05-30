package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class UsernameRequiredException extends BaseException{
    public UsernameRequiredException() {super(HttpStatus.BAD_REQUEST, "사용자명을 입력해주세요.");
    }
}