package com.woven.movie_uploader.controllers;


import com.woven.movie_uploader.filehandler.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class FileUploadControllerTest {

    // only accept video/mp4,video/mpeg format as written in application.properties
    private MockMvc mockMvc;

    @MockBean
    private FileStorage storage;
    @Autowired
    private FileUploadController controller;

    private final String sampleFileName = "sample1e";
    private final byte[] sampleContents = "1234567".getBytes(StandardCharsets.UTF_8);
    private final String sampleSupportedContentType = "video/mpeg";
    private final String sampleUnsupportedContentType = "application/json";

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testUploadSuccess() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(
                "data",
                sampleFileName,
                sampleSupportedContentType,
                sampleContents
        );
        final String sampleId = "id123";
        when(storage.uploadFile(eq(sampleFileName), any(), eq(sampleSupportedContentType))).thenReturn(sampleId);

        mockMvc.perform(multipart("/v1/files").file(mockFile))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "v1/files/" + sampleId))
                .andExpect(content().string("File uploaded"));
        verify(storage, times(1))
                .uploadFile(eq(sampleFileName), any(), eq(sampleSupportedContentType));
    }

    @Test
    public void testUnsupportedMediaTypes() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(
                "data",
                sampleFileName,
                sampleUnsupportedContentType,
                sampleContents
        );

        mockMvc.perform(multipart("/v1/files").file(mockFile))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().string("Unsupported Media Type"));

    }
}
