package org.example.newsfeed.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


/**
 * 회원가입 요청을 받는 DTO 클래스 입니다
 * 이메일과 비밀번호, 사용자 이름을 포함하며 유효성 감사 조건이 적용되어 있습니다.
 */
@Getter
public class SignupRequestDto {

    /**
     * 사용자 이메일
     * 비어있을 수 없으며, 이메일 형식을 지켜야 합니다.
     */
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;


    /**
     * 사용자 비밀번호
     * 비어있을 수 없으며, 최소 8자 이상 입력해야 합니다.
     */
    @Size(min = 8, message = "최소 8자 이상 입력해주세요!")
    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;


    /**
     * 사용자 이름
     * 비어있을 수 없습니다.
     */
    @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
    private String username;
}
