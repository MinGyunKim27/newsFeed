package org.example.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

public class User {

    @Id // primary 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // null 불가 추가해줘야함
    private String username;

    @Column
    private String email;

    @Column
    private String password;

}
