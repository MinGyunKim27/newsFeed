package org.example.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto findById(Long id) {
        User byId = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(HttpStatus.NOT_FOUND,"사용자가 존재하지 않음"));

        return new UserResponseDto(byId);
    }
}
