package org.example.newsfeed.global.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * 비밀번호 암호화 및 비교를 담당하는 유틸리티 클래스입니다.
 * BCrypt 알고리즘을 사용하여 비밀번호를 해싱합니다.
 */
@Component
public class PasswordEncoder {

    /**
     * 주어진 원문 비밀번호를 BCrypt 알고리즘으로 암호화합니다.
     *
     * @param rawPassword 사용자가 입력한 평문 비밀번호
     * @return 암호화된 비밀번호 문자열
     */
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * 입력한 평문 비밀번호와 암호화된 비밀번호가 일치하는지 검사합니다.
     *
     * @param rawPassword 사용자가 입력한 평문 비밀번호
     * @param encodedPassword 저장된 암호화된 비밀번호
     * @return 일치하면 true, 그렇지 않으면 false
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
