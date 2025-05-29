package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BaseException {
    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }
}
