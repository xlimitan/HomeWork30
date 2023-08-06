package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    @Value("${path.to.avatars.folder}")
    private Path avatarPath;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar getById (Long id){
        return avatarRepository.findById(id).orElseThrow();
    }

    public Long save(Long studentId, MultipartFile multipartFile) throws IOException {
//        STEP1, создаем папку(директорию)
        Files.createDirectories(avatarPath);
//        STEP2, создать путь к файлу и сам файл.
//        Кладем в переменную индекс точки для нахождения расширения файла.
        int dotIndex = multipartFile.getOriginalFilename().lastIndexOf(".");
//        Кладем в переменную название расширения(т.е. само расширение).
        String fileExtension = multipartFile.getOriginalFilename().substring(dotIndex + 1);
//        Создали путь к файлу и его название.
        Path path = avatarPath.resolve(studentId + "." + fileExtension);
//        Записываем данные в файл(указываем путь, вытаскиваем данные, указываем параметр(если файл есть, то будет перезаписан, если нет, то будет создан)).
        byte[] data = multipartFile.getBytes();
        Files.write(path, data, StandardOpenOption.CREATE);

//        STEP3
        Student studentReference = studentRepository.getReferenceById(studentId);
        Avatar avatar = avatarRepository.findFirstByStudent(studentReference).orElse(new Avatar());
        avatar.setStudent(studentReference);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setData(data);
        avatar.setFilePath(path.toAbsolutePath().toString());
        avatarRepository.save(avatar);
        return avatar.getId();
    }
}
