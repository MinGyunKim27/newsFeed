package org.example.newsfeed.user.repository;

import org.example.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일 중복 체크를 위해 추가

    boolean existsByEmail(String email);
}
