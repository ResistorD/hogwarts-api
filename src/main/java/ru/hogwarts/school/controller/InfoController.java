package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.LongStream;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final int port;

    public InfoController(@Value("${server.port}") int port) {
        this.port = port;
    }

    @GetMapping("/port")
    public int getPort() {
        return port;
    }

    @GetMapping("/sum-fast")
    @ResponseStatus(HttpStatus.OK)
    public long getFastSum() {
        return LongStream.rangeClosed(1, 1_000_000)
                .parallel()                 // параллелим
                .sum();                     // сумма long
    }
}
