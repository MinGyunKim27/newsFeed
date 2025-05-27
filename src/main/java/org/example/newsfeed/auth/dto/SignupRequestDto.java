package org.example.newsfeed.auth.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;

    private String password;

    private String username;
}
