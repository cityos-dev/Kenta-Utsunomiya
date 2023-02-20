package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileStorage;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class FileDeleteControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FileStorage storage;
    @Autowired
    private FileDeleteController controller;

    @BeforeEach
    public void startUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        final String fileid = "test_id";
        when(storage.deleteFile(eq(fileid))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/files/"+ fileid))
                .andExpect(status().isNoContent())
                .andExpect(result -> Assertions.assertEquals("File was successfully removed",
                        result.getResponse().getContentAsString()));
        verify(storage, times(1)).deleteFile(eq(fileid));
    }

    @Test
    public void testNotExist() throws Exception {
        final String fileid = "not_exist";
        when(storage.deleteFile(any())).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/files/" + fileid))
                .andExpect(status().isNotFound())
                .andExpect(content().string("File not found"));
    }

}
