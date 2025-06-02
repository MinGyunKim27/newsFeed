package org.example.newsfeed.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.post.entity.Post;

@Table(name = "image")
@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "image_url",length = 500,nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String imageUrl){
        this.imageUrl = imageUrl;
    };

    public Image() {
    }
}
