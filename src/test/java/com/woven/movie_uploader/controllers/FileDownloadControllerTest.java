package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileMetadata;
import com.woven.movie_uploader.filehandler.FileStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class FileDownloadControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private FileStorage storage;
    @Autowired
    private FileDownloadController controller;

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testDownloadSuccess() throws Exception {
        final String fileid = "fileid";
        final String filename = "Test.mp4";
        final String contentType = "video/mp4";
        final byte[] sampleContent = "12345".getBytes(StandardCharsets.UTF_8);
        final String createdAt = "2023-02-20T01:52:00Z";
        final FileMetadata dummyFile = FileMetadata.builder()
                .setFileId(fileid)
                .setName(filename)
                .setContent(sampleContent)
                .setContentType(contentType)
                .setCreatedAt(createdAt)
                .setFilesize(sampleContent.length)
                .build();
        when(storage.getFileContents(eq(fileid))).thenReturn(Optional.of(dummyFile));
        when(storage.getFileResource(eq(fileid))).thenReturn(new ByteArrayResource(sampleContent));
        final byte[] result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/files/"+ fileid))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, contentType))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", filename)))
                        .andReturn().getResponse().getContentAsByteArray();

        Assertions.assertArrayEquals(sampleContent, result);
        verify(storage, times(1)).getFileContents(eq(fileid));
        verify(storage, times(1)).getFileResource(eq(fileid));
    }

    @Test
    public void testDownloadNotfound() throws Exception {
        final String fileid = "notfound";
        when(storage.getFileContents(eq(fileid))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/files/" + fileid))
                .andExpect(status().isNotFound())
                .andExpect(content().string("File not found"));
        verify(storage, times(1)).getFileContents(eq(fileid));
    }

}
