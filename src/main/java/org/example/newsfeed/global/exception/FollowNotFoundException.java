package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class FollowNotFoundException extends BaseException{
    public FollowNotFoundException() {
        super(HttpStatus.NOT_FOUND, "팔로우가 존재하지 않습니다.");
    }
}
