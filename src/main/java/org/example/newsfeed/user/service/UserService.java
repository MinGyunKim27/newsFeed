package org.example.newsfeed.user.service;


import jakarta.servlet.http.HttpSession;
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
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(user);
    }

    // 프로필 수정
    public UserResponseDto updateProfile(HttpSession session, ProfileUpdateRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        user.updateProfile(requestDto.getUsername(), requestDto.getProfileImage(), requestDto.getBio());
        return new UserResponseDto(user);
    }

    // 비밀번호 변경
    public UserResponseDto updatePassword(HttpSession session, PasswordUpdateRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        if(!user.getPassword().equals(requestDto.getCurrentPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(requestDto.getNewPassword());
        return new UserResponseDto(user);

    }

}