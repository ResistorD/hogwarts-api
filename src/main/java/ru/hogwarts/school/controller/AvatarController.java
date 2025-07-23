package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<Long> uploadAvatar(@PathVariable Long studentId,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(avatar.getId());
    }

    @GetMapping("/from-db/{id}")
    public ResponseEntity<byte[]> downloadFromDb(@PathVariable Long id) {
        byte[] data = avatarService.downloadFromDb(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // можно адаптировать
        return ResponseEntity.ok().headers(headers).body(data);
    }

    @GetMapping("/from-file/{id}")
    public ResponseEntity<byte[]> downloadFromFile(@PathVariable Long id) throws IOException {
        byte[] data = avatarService.downloadFromFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
