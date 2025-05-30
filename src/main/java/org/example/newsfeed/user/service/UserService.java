package org.example.newsfeed.user.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.repository.FollowRepository;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.PasswordNotMatchException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.global.exception.UsernameRequiredException;
import org.example.newsfeed.global.util.RequestToId;
import org.example.newsfeed.user.dto.*;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.newsfeed.global.util.RequestToId.requestToId;


@Service
@RequiredArgsConstructor //final 필드 자동 생성
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    // 프로필 조회
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(UserNotFoundException::new);

        // followersCount 포함해서 Dto 반환
        long followersCount = followRepository.countByFollowingId(userId);
        return new UserResponseDto(user, followersCount);

    }

    // 프로필 수정
    public UserResponseDto updateProfile(Long id, ProfileUpdateRequestDto requestDto) {

        // username이 비었을 경우 예외처리
        if (requestDto.getUsername() == null || requestDto.getUsername().trim().isEmpty()) {
            throw new UsernameRequiredException();
        }

        User byId = userRepository.findById(id).
                orElseThrow(UserNotFoundException::new);

        byId.updateProfile(requestDto.getUsername(), requestDto.getProfileImage(), requestDto.getBio());

        userRepository.save(byId);

        // followersCount 포함해서 응답
        long followersCount = followRepository.countByFollowerId(id);
        return new UserResponseDto(byId, followersCount);
    }

    // 비밀번호 변경
    public UserResponseDto updatePassword(Long id, PasswordUpdateRequestDto requestDto) {

        User user = userRepository.findById(id).
                orElseThrow(UserNotFoundException::new);

        // password가 일치하지 않았을 경우 예외처리
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
            throw new PasswordNotMatchException();
        }

        user.updatePassword(requestDto.getNewPassword());

        userRepository.save(user);

        // followersCount 포함해서 응답
        long followersCount = followRepository.countByFollowerId(id);
        return new UserResponseDto(user, followersCount);
    }


    public Page<UserResponseDto> findUsers(String username, Pageable pageable) {
        Page<User> usersPage = userRepository.findByUsernameContaining(username, pageable);

        return usersPage.map(user -> {long followersCount = followRepository.countByFollowingId(user.getId());
            return new UserResponseDto(user, followersCount);
        });
    }
}