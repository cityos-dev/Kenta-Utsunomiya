package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.filehandler.FileInMongoStorage;
import com.woven.movie_uploader.filehandler.FileStorage;
import com.woven.movie_uploader.mongo.FileRepository;
import org.bson.codecs.ObjectIdGenerator;
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
    FileStorage fileHandler(final FileRepository repository) {
        // return new FileInMemoryHandler(messageDigest);
        return new FileInMongoStorage(repository, new ObjectIdGenerator());
    }

}
