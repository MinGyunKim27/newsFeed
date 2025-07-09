package org.example.newsfeed.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


/**
 * 회원 탈퇴 요청을 처리하기 위한 DTO 클래스
 * 비밀번호만을 포함하며 유효성 검사 조건이 적용되어 있슴니다.
 */
@Getter
public class WithdrawRequestDto {
    /**
     * 사용자 비밀번호
     * 비밀번호는 비어있을 수 없습니다.
     * 최소 8자리 이상 입력해야 합니다.
     */
    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8,message = "비밀번호는 최소 8자리 이상 입력해야 합니다.")
    private String password;
}
