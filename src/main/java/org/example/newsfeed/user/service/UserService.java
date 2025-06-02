package org.example.newsfeed.user.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.PasswordNotMatchException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.global.util.RequestToId;
import org.example.newsfeed.user.dto.*;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.example.newsfeed.global.util.RequestToId.requestToId;


@Service
@RequiredArgsConstructor //final 필드 자동 생성
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowService followService;

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

    public List<UserResponseDto> findUsers(String username,Long id) {

        return userRepository.findByUsernameContainingAndIdIsNot(username,id)
                .stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    public List<UserResponseDto> findUsersWithFollowerCount(String keyword, Long currentUserId) {
        List<UserResponseDto> users = findUsers(keyword, currentUserId);

        List<Long> userIds = users.stream()
                .map(UserResponseDto::getUserId)
                .toList();

        Map<Long, Long> followerCountMap = followService.getFollowerCounts(userIds);

        for (UserResponseDto dto : users) {
            Long count = followerCountMap.getOrDefault(dto.getUserId(), 0L);
            dto.setFollowerCount(count);
        }

        return users;
    }

}