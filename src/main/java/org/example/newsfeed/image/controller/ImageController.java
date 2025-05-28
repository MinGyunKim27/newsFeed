package org.example.newsfeed.image.controller;

import org.example.newsfeed.image.exception.ImageUploadException;
import org.example.newsfeed.image.util.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final String uploadDir = "C:/upload/images/";

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ImageUploadException("파일이 비어 있습니다.");
            }

            if (!FileUtils.isImage(file.getContentType())) {
                throw new ImageUploadException("이미지 파일만 업로드할 수 있습니다.");
            }

            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = FileUtils.generateUniqueFileName(file.getOriginalFilename());
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            return "파일 업로드 성공: " + fileName;
        } catch (IOException e) {
            throw new ImageUploadException("파일 저장 중 오류 발생", e);
        }
    }
}
