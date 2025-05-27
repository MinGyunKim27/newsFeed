package org.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.newsfeed.global.common.entity.BaseEntity;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    private String username;


    private String profileImage;


    private String bio;

    public User(String email,String password,String username){
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
