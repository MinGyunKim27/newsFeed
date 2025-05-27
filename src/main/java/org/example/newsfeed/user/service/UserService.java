package org.example.newsfeed.user.service;


import lombok.RequiredArgsConstructor;
import org.example.newsfeed.user.dto.*;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //final 필드 자동 생성
public class UserService {

    private final UserRepository userRepository;

    // 프로필 조회
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(user);
    }

    // 프로필 수정
    public void updateProfile(Long id, ProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        user.updateProfile(requestDto.getUsername());
        userRepository.save(user);
    }

    // 비밀번호 변경
    public void updatePassword(Long id, PasswordUpdateRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        String current = requestDto.getCurrentPassword();
        String next = requestDto.getNewPassword();

        if (!user.getPassword().equals(current)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (current.equals(next)) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");
        }

        if (!isValidPassword(next)) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }

        user.updatePassword(next);
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
}