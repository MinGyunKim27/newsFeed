package org.example.newsfeed.user.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.PasswordNotMatchException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.dto.*;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor //final 필드 자동 생성
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 프로필 조회
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new UserResponseDto(user);
    }

    // 프로필 수정
    public UserResponseDto updateProfile(Long id, ProfileUpdateRequestDto requestDto) {
        User byId = userRepository.findById(id).
                orElseThrow(UserNotFoundException::new);

        byId.updateProfile(requestDto.getUsername(), requestDto.getProfileImage(), requestDto.getBio());

        userRepository.save(byId);

        return new UserResponseDto(byId);
    }

    // 비밀번호 변경
    public UserResponseDto updatePassword(Long id, PasswordUpdateRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
            throw new PasswordNotMatchException();
        }

        user.updatePassword(requestDto.getNewPassword());

        userRepository.save(user);
        return new UserResponseDto(user);
    }

}