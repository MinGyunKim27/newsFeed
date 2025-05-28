package org.example.newsfeed.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class imageController {
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "C:/upload/images/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        File dest = new File(uploadDir + fileName);
        file.transferTo(dest);

        return "파일 업로드 성공: " + fileName;
    }


}
