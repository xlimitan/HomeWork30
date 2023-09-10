package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private static final Logger logger = LoggerFactory.getLogger(AvatarController.class);

    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;

    public AvatarController(AvatarService avatarService,
                            AvatarRepository avatarRepository) {
        this.avatarService = avatarService;
        this.avatarRepository = avatarRepository;
    }

    @GetMapping("/from-disk/{id}")
    public void fromDisk(@PathVariable Long id, HttpServletResponse response) {
        Avatar avatar = avatarService.getById(id);
        response.setContentType(avatar.getMediaType());
//        response.setContentLength((int) avatar.getFileSize());
        try(FileInputStream fis = new FileInputStream(avatar.getFilePath())) {
        fis.transferTo(response.getOutputStream());
        } catch (IOException e) {
            logger.error("Failed to download avatar with id = "+id, e);
        }


        }
    @GetMapping("/from-db/{id}")
    public ResponseEntity<byte[]> fromDd(@PathVariable Long id){
//        Получаем аватар из базы данных
        Avatar avatar = avatarService.getById(id);
//        Создаем заголовки
        HttpHeaders headers = new HttpHeaders();
//        Передаем тип данных
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
//        Передаем размер
        headers.setContentLength(avatar.getFileSize());
//        Возвращаем ответ
        return ResponseEntity.status(200).headers(headers).body(avatar.getData());
    }

    @GetMapping("/page")
    public List<AvatarDto> getPage(@RequestParam("page") Integer num,
                                   @RequestParam("size") Integer size) {
        return avatarService.getPage(num, size);
    }

}
