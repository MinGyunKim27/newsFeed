package org.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.newsfeed.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id // primary 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    private String bio;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void updateProfile(String username, String profileImage, String bio) {
        this.username = username;
        this.profileImage = profileImage;
        this.bio = bio;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}