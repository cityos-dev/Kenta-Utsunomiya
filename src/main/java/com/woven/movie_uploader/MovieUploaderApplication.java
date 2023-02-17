package com.woven.movie_uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"com.woven.movie_uploader.controllers", "com.woven.movie_uploader.components"})
public class MovieUploaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieUploaderApplication.class, args);
    }
}
