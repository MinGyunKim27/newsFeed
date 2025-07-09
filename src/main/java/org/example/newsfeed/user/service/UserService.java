package org.example.newsfeed.user.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.PasswordNotMatchException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.dto.*;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.example.newsfeed.global.util.RequestToId.requestToId;

/**
 * 사용자(User) 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 * 사용자 조회, 수정, 비밀번호 변경, 검색 등 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor // final 필드를 자동으로 생성자 주입해줍니다.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowService followService;

    /**
     * 사용자 프로필 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보 DTO
     */
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return new UserResponseDto(user);
    }

    /**
     * 사용자 프로필 수정
     *
     * @param id 수정 대상 사용자 ID
     * @param requestDto 수정할 사용자 정보 (username, bio, profileImage)
     * @return 수정된 사용자 정보 DTO
     */
    public UserResponseDto updateProfile(Long id, ProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updateProfile(requestDto.getUsername(), requestDto.getProfileImage(), requestDto.getBio());

        userRepository.save(user); // 변경 감지를 활용한 저장

        return new UserResponseDto(user);
    }

    /**
     * 비밀번호 변경
     *
     * @param id 사용자 ID
     * @param requestDto 현재 비밀번호와 새 비밀번호 정보
     * @return 변경된 사용자 정보 DTO
     * @throws PasswordNotMatchException 현재 비밀번호 불일치 시
     */
    public UserResponseDto updatePassword(Long id, PasswordUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        // 새 비밀번호로 업데이트
        user.updatePassword(requestDto.getNewPassword());

        userRepository.save(user);

        return new UserResponseDto(user);
    }

    /**
     * 키워드에 따라 사용자 목록을 페이징으로 검색하고,
     * 각 사용자에 대해 팔로워 수를 포함하여 반환합니다.
     *
     * @param keyword 검색 키워드 (username 포함)
     * @param currentUserId 현재 로그인한 사용자 ID (자기 자신 제외용)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지당 항목 수
     * @return 팔로워 수가 포함된 사용자 응답 DTO 페이지
     */
    public Page<UserResponseDto> findUsersWithFollowerCount(String keyword, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 사용자 이름에 키워드를 포함하고, 자기 자신은 제외한 사용자 페이지 조회
        Page<User> userPage = userRepository.findByUsernameContainingAndIdIsNot(keyword, currentUserId, pageable);

        // 사용자 ID 목록 추출
        List<Long> userIds = userPage.stream()
                .map(User::getId)
                .toList();

        // 사용자별 팔로워 수 조회
        Map<Long, Long> followerCountMap = followService.getFollowerCounts(userIds);

        // 사용자 응답 DTO에 팔로워 수 추가하여 반환
        return userPage.map(user -> {
            UserResponseDto dto = new UserResponseDto(user);
            dto.setFollowerCount(followerCountMap.getOrDefault(user.getId(), 0L));
            return dto;
        });
    }

}
