package com.woven.movie_uploader;

import com.woven.movie_uploader.mongo.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.woven.movie_uploader.controllers",
        "com.woven.movie_uploader.components",
        "com.woven.movie_uploader.properties",
        "com.woven.movie_uploader.mongo"})
@EnableMongoRepositories
public class MovieUploaderApplication {

    @Autowired
    private FileRepository fileRepository;


    public static void main(String[] args) {
        SpringApplication.run(MovieUploaderApplication.class, args);
    }
}
