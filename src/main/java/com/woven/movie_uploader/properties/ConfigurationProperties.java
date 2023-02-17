package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.components.FileInMemoryHandler;
import com.woven.movie_uploader.filehandler.FileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.security.MessageDigest;

@Configuration
@ComponentScan
public class ConfigurationProperties {


    @Bean
    MessageDigest messageDigest() throws Exception {
        return MessageDigest.getInstance("md5");
    }

    @Bean
    FileHandler fileHandler(final MessageDigest messageDigest) throws Exception {
        return new FileInMemoryHandler(messageDigest);
    }

}
