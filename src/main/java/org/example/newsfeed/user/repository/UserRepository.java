package org.example.newsfeed.user.repository;

import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    int countByUsername(String username);

    double countByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByUsernameContaining(String username);

    //페이징 조회용
    Page<User> findByUsernameContaining(String username, Pageable pageable);

}
