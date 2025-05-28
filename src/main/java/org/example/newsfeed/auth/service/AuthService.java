package org.example.newsfeed.auth.service;


import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.dto.WithdrawRequestDto;
import org.example.newsfeed.global.config.PasswordEncoder;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void signup(SignupRequestDto requestDto){
        String encodedPassword = encoder.encode(requestDto.getPassword());

        if (userRepository.countByUsername(requestDto.getUsername()) > 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"사용자 이름이 중복되었습니다!");
        }
        if (userRepository.countByEmail(requestDto.getEmail()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이메일이 중복되었습니다!");
        }

        User user = new User(requestDto.getEmail(), encodedPassword, requestDto.getUsername());
        userRepository.save(user);
    }

    public User login(LoginRequestDto requestDto) {
        User byEmail = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"사용자가 존재하지 않습니다!"));

        if (!encoder.matches(requestDto.getPassword(), byEmail.getPassword())){
            throw new  ResponseStatusException(HttpStatus.CONFLICT,"비밀번호가 일치하지 않습니다!");
        }

        return byEmail;
    }

    public void withdraw(WithdrawRequestDto withdrawRequestDto, Long id) {
        User byId = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(HttpStatus.NOT_FOUND,"사용자가 존재하지 않음"));

        if (!encoder.matches(withdrawRequestDto.getPassword(), byId.getPassword())){
            throw new  ResponseStatusException(HttpStatus.CONFLICT,"비밀번호가 일치하지 않습니다!");
        }

        userRepository.delete(byId);
    }
}
