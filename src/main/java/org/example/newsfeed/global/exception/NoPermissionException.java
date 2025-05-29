package org.example.newsfeed.global.exception;

import org.springframework.http.HttpStatus;

public class NoPermissionException extends BaseException {

    public NoPermissionException() {
        super(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
}
