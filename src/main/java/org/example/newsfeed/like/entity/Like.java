package org.example.newsfeed.like.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "like")

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
