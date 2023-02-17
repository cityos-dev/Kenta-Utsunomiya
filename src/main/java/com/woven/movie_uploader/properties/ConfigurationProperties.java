package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.components.FileInMongoHandler;
import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.mongo.FileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;

@Configuration
public class ConfigurationProperties {

    @Bean
    MessageDigest messageDigest() throws Exception {
        return MessageDigest.getInstance("md5");
    }

    @Bean
    FileHandler fileHandler(final MessageDigest messageDigest, final FileRepository repo) {
        return new FileInMongoHandler(repo, messageDigest);
    }

}
