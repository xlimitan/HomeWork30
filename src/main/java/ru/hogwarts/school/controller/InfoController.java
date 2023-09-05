package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
public class InfoController {

    @Value("${server.port:-1}")
    private int port;

    @GetMapping("/port")
        public int getPort(){
        return port;
    }
}
