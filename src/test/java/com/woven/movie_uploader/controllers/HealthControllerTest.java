package com.woven.movie_uploader.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthControllerTest {
    @Autowired
    private HealthController controller;
    @Value(value="${local.server.port}")
    private int port;


    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

}
