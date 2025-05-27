package org.example.newsfeed.user.repository;

import org.example.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    int countByUsername(String username);

    double countByEmail(String email);

    Optional<User> findByEmail(String email);
}
