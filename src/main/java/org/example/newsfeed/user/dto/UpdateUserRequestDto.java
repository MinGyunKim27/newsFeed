package org.example.newsfeed.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String username;

    private String profileImage;

    private String bio;
}
