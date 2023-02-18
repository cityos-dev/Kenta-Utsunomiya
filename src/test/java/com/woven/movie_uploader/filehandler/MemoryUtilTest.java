package com.woven.movie_uploader.filehandler;

import org.bson.codecs.ObjectIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemoryUtilTest {

    private FileInMemoryHandler storageUtil;
    private ObjectIdGenerator objectIdGenerator;

    @BeforeEach
    public void startUp() throws Exception {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        storageUtil = new FileInMemoryHandler(objectIdGenerator);
    }

    @Test
    public void testUpdateExistDelete() throws IOException {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String id = "aabbcc";
        when(objectIdGenerator.generate()).thenReturn(id);
        assertEquals(id, storageUtil.uploadFile(filename, content, contentType));
        // upload

        assertTrue(storageUtil.getFileContents(id).isPresent());

        // delete file
        assertTrue(storageUtil.deleteFile(id));


        assertFalse(storageUtil.getFileContents(id).isPresent());
    }

    @Test
    public void testFileDownload() throws IOException {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String id = "aabbcc";
        when(objectIdGenerator.generate()).thenReturn(id);
        assertEquals(id, storageUtil.uploadFile(filename, content, contentType));
        verify(objectIdGenerator, times(1)).generate();

        final Resource resource = storageUtil.getFileResource(id);
        assertNotNull(resource);
        assertEquals(content.length, resource.contentLength());
    }
}
