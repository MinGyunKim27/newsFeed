package org.example.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.UpdateUserRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto findById(Long id) {
        User byId = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(HttpStatus.NOT_FOUND,"사용자가 존재하지 않음"));

        return new UserResponseDto(byId);
    }

    public UserResponseDto update(Long id, UpdateUserRequestDto requestDto) {
        User byId = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(HttpStatus.NOT_FOUND,"사용자가 존재하지 않음"));

        byId.updateUser(requestDto.getUsername(), requestDto.getProfileImage(), requestDto.getBio());

        userRepository.save(byId);

        return new UserResponseDto(byId);
    }

    public void updatePassword(Long id, PasswordUpdateRequestDto requestDto) {
        User byId = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(HttpStatus.NOT_FOUND,"사용자가 존재하지 않음"));

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), byId.getPassword())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"비밀번호가 일치하지 않습니다!");
        }

        byId.updatePassword(requestDto.getNewPassword());

        userRepository.save(byId);
    }
}
