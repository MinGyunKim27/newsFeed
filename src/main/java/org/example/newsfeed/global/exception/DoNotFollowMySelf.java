package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class DoNotFollowMySelf extends BaseException {
    public DoNotFollowMySelf() {
        super(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다.");
    }
}
