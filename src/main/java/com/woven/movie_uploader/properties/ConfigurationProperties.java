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

@Configuration
public class ConfigurationProperties {

    @Bean
    ObjectIdGenerator objectIdGenerator() {
        return new ObjectIdGenerator();
    }

    @Bean
    FileStorage fileHandler(final FileRepository repository,
                            @Value("${file.storage.implementation}") final FileStorageImplName storageImplName,
                            final ObjectIdGenerator objectIdGenerator) {
        // change the storage based on the configuration.
        return switch (storageImplName) {
            case MEMORY -> new FileInMemoryStorage(objectIdGenerator);
            case MONGO -> new FileInMongoStorage(repository, objectIdGenerator);
        };
    }

}
