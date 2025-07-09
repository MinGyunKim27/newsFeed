package org.example.newsfeed.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 비밀번호 수정 요청 시 사용되는 DTO 클래스입니다.
 * 현재 비밀번호와 새 비밀번호를 함께 전달받아 검증 및 갱신에 사용됩니다.
 */
@Getter
public class PasswordUpdateRequestDto {

    /**
     * 현재 비밀번호입니다.
     * 최소 8자 이상이어야 하며, 유효성 검사가 적용됩니다.
     */
    @Size(min = 8, message = "비밀번호는 최소 8자리를 입력해 주세요")
    private String currentPassword;

    /**
     * 새 비밀번호입니다.
     * 최소 8자 이상이어야 하며, 유효성 검사가 적용됩니다.
     */
    @Size(min = 8, message = "비밀번호는 최소 8자리를 입력해 주세요")
    private String newPassword;
}
