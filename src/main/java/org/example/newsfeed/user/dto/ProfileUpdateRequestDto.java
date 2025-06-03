package org.example.newsfeed.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 프로필 수정 요청 DTO
 * 사용자가 자신의 프로필 정보를 수정할 때 사용되는 데이터 전달 객체입니다.
 */
@Getter
public class ProfileUpdateRequestDto {

    /**
     * 사용자 이름 (닉네임 등)
     * null 불가 — 반드시 입력되어야 합니다.
     */
    @NotNull
    private String username;

    /**
     * 프로필 이미지 URL
     * 선택 항목이며, null이어도 무방합니다.
     */
    private String profileImage;

    /**
     * 사용자 소개 (자기소개 텍스트)
     * 최대 200자까지 입력 가능
     */
    @Size(max = 200)
    private String bio;
}
