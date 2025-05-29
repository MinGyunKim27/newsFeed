package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class LikeNotFoundException extends BaseException {

    public LikeNotFoundException() {
        super(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다.");
    }
}
