package com.woven.movie_uploader.properties;

import com.woven.movie_uploader.components.MemoryUtil;
import com.woven.movie_uploader.filehandler.FileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ConfigurationProperties {



    @Bean
    FileHandler fileHandler() throws Exception {
        return new MemoryUtil();
    }

}
