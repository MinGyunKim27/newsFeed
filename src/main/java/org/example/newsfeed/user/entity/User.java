package org.example.newsfeed.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String username;
}
