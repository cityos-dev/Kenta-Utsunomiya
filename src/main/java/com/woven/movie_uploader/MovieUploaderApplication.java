package com.woven.movie_uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = {"com.woven.movie_uploader.controllers", "com.woven.movie_uploader.components", "com.woven.movie_uploader.properties"})
public class MovieUploaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieUploaderApplication.class, args);
    }
}
