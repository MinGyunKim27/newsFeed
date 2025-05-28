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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void signup(SignupRequestDto requestDto){
        String encodedPassword = encoder.encode(requestDto.getPassword());

        if (userRepository.countByUsername(requestDto.getUsername()) > 0){
            throw new UserNameDuplicatesException();
        }
        if (userRepository.countByEmail(requestDto.getEmail()) > 0) {
            throw new EmailDuplicatesException();
        }

        User user = new User(requestDto.getEmail(), encodedPassword, requestDto.getUsername());
        userRepository.save(user);
    }

    public User login(LoginRequestDto requestDto) {
        User byEmail = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(
                        UserNotFoundException::new);

        if (!encoder.matches(requestDto.getPassword(), byEmail.getPassword())){
            throw new PasswordNotMatchException();
        }

        return byEmail;
    }

    public void withdraw(WithdrawRequestDto withdrawRequestDto, Long id) {
        User byId = userRepository.findById(id).
                orElseThrow(
                        UserNotFoundException::new);

        if (!encoder.matches(withdrawRequestDto.getPassword(), byId.getPassword())){
            throw new  PasswordNotMatchException();
        }

        userRepository.delete(byId);
    }
}
