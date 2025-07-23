package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        File folder = new File("avatars");
        if (!folder.exists()) {
            boolean created = folder.mkdir();
            if (!created) {
                throw new IOException("Не удалось создать директорию для аватаров");
            }
        }

        String path = "avatars/" + studentId + "_" + file.getOriginalFilename();
        File dest = new File(path);
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(file.getBytes());
        }

        Avatar avatar = avatarRepository.findAvatarByStudentId(studentId);
        if (avatar == null) {
            avatar = new Avatar();
            avatar.setStudent(student);
        }
        avatar.setFilePath(dest.getPath());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        return avatarRepository.save(avatar);
    }


    public byte[] downloadFromDb(Long id) {
        return avatarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not found"))
                .getData();
    }

    public byte[] downloadFromFile(Long id) throws IOException {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not found"));

        return Files.readAllBytes(new File(avatar.getFilePath()).toPath());
    }
}
