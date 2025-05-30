package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class UsernameSearchRequiredException extends BaseException {
    public UsernameSearchRequiredException() {super(HttpStatus.BAD_REQUEST, "사용자명을 검색해 주세요.");
    }
}
