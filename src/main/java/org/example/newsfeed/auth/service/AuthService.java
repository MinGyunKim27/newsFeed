package org.example.newsfeed.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.dto.WithdrawRequestDto;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.EmailDuplicatesException;
import org.example.newsfeed.global.exception.PasswordNotMatchException;
import org.example.newsfeed.global.exception.UserNameDuplicatesException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * 인증 및 회원가입 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    /**
     * 회원가입 기능을 수행합니다.
     * - 사용자명 중복 체크
     * - 이메일 중복 체크
     * - 비밀번호 암호화 후 저장
     *
     * @param requestDto 회원가입 요청 정보를 담은 DTO
     * @throws UserNameDuplicatesException 사용자명이 이미 존재할 경우 발생
     * @throws EmailDuplicatesException 이메일이 이미 존재할 경우 발생
     */
    public void signup(SignupRequestDto requestDto){
        String encodedPassword = encoder.encode(requestDto.getPassword());

        if (userRepository.countByUsername(requestDto.getUsername()) > 0){
            throw new UserNameDuplicatesException();
        }
        if (userRepository.countByEmail(requestDto.getEmail()) > 0) {
            throw new EmailDuplicatesException();
        }

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), encodedPassword);
        userRepository.save(user);
    }

    /**
     * 로그인 기능을 수행합니다.
     * - 이메일로 사용자 조회
     * - 비밀번호 일치 여부 확인
     *
     * @param requestDto 로그인 요청 정보를 담은 DTO
     * @return 로그인에 성공한 사용자 객체
     * @throws UserNotFoundException 이메일로 사용자를 찾을 수 없을 경우 발생
     * @throws PasswordNotMatchException 비밀번호가 일치하지 않을 경우 발생
     */
    public User login(LoginRequestDto requestDto) {
        User byEmail = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(requestDto.getPassword(), byEmail.getPassword())){
            throw new PasswordNotMatchException();
        }

        return byEmail;
    }

    /**
     * 회원 탈퇴 기능을 수행합니다.
     * - 사용자 ID로 사용자 조회
     * - 입력한 비밀번호와 실제 비밀번호 일치 여부 확인
     * - 일치할 경우 사용자 삭제
     *
     * @param withdrawRequestDto 탈퇴 요청 정보를 담은 DTO
     * @param id 탈퇴 요청한 사용자의 ID
     * @throws UserNotFoundException ID로 사용자를 찾을 수 없을 경우 발생
     * @throws PasswordNotMatchException 비밀번호가 일치하지 않을 경우 발생
     */
    public void withdraw(WithdrawRequestDto withdrawRequestDto, Long id) {
        User byId = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(withdrawRequestDto.getPassword(), byId.getPassword())){
            throw new PasswordNotMatchException();
        }

        userRepository.delete(byId);
    }
}
