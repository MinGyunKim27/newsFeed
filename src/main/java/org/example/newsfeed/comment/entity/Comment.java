package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import org.example.newsfeed.global.common.entity.BaseEntity;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
