package com.woven.movie_uploader.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest
@AutoConfigureMockMvc
public class HealthControllerTest {

    @Autowired
    private HealthController controller;





    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


}
