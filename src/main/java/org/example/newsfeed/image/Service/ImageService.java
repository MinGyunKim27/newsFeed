package org.example.newsfeed.image.Service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.PostNotFoundException;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;
import org.example.newsfeed.image.entity.Image;
import org.example.newsfeed.image.repository.ImageRepository;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageUploadResponseDto saveImage(String fileName) {
        Image image = new Image(fileName);
        imageRepository.save(image);

        return new ImageUploadResponseDto(image.getId(),fileName);
    }
}
