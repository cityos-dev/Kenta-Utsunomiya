package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")

public class ErrorHandleControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private FileStorage storage;
    @Autowired
    private FileUploadController controller;
    @Autowired
    private ErrorHandleController errorController ;

    @BeforeEach
    public void setUp() {
        mockMvc= MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(errorController)
                .build();
    }

    @Test
    public void testCheckNotAllowedNodata() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/files"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request."));
    }

    @Test
    public void testCheckNotAllowedEmptyData() throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/files")
                .header("Content-Type", "multipart/form-data"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request."));
    }
}
