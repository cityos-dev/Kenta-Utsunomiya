package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.filehandler.FileInMemoryStorage;
import com.woven.movie_uploader.filehandler.FileInMongoStorage;
import com.woven.movie_uploader.filehandler.FileStorage;
import com.woven.movie_uploader.filehandler.FileStorageImplName;
import com.woven.movie_uploader.mongo.FileRepository;
import org.bson.codecs.ObjectIdGenerator;
import org.springframework.beans.factory.annotation.Value;
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
    FileStorage fileHandler(final FileRepository repository,
                            @Value("${file.storage.implementation}") final FileStorageImplName storageImplName) {
        // change the storage based on the configuration.
        return switch (storageImplName) {
            case MEMORY -> new FileInMemoryStorage(new ObjectIdGenerator());
            case MONGO -> new FileInMongoStorage(repository, new ObjectIdGenerator());
        };
    }

}
