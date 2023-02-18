package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.filehandler.FileInMongoHandler;
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
    FileHandler fileHandler(final FileRepository repository, final MessageDigest messageDigest) {
        // return new FileInMemoryHandler(messageDigest);
        return new FileInMongoHandler(repository, messageDigest);
    }

}
