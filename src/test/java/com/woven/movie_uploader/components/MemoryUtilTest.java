package com.woven.movie_uploader.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemoryUtilTest {

    private FileInMemoryHandler storageUtil;
    private MessageDigest mockDigest;

    private final String sampleMd5String = "00112233445566778899aabbccddeeff";
    private byte[] sampleM5byteArray;
    @BeforeEach
    public void tearUp() throws Exception {
        mockDigest = mock(MessageDigest.class);
        storageUtil = new FileInMemoryHandler(mockDigest);
        sampleM5byteArray = new byte[16];
        for(int i = 0 ; i < 16; i++) {
            sampleM5byteArray[i] = (byte)(i * 16 + i);
        }
    }

    @Test
    public void testUpdateExistDelete() throws IOException {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        when(mockDigest.digest(content)).thenReturn(sampleM5byteArray);
        assertEquals(sampleMd5String, storageUtil.uploadFile(filename, content, contentType));
        verify(mockDigest, times(1)).digest(content);
        // upload

        assertTrue(storageUtil.getFileContents(sampleMd5String).isPresent());

        // delete file
        assertTrue(storageUtil.deleteFile(sampleMd5String));


        assertFalse(storageUtil.getFileContents(sampleMd5String).isPresent());
        assertFalse(storageUtil.getFileContents(sampleMd5String).isPresent());
    }

    @Test
    public void testFileDownload() throws IOException {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        when(mockDigest.digest(content)).thenReturn(sampleM5byteArray);
        assertEquals(sampleMd5String, storageUtil.uploadFile(filename, content, contentType));
        verify(mockDigest, times(1)).digest(content);

        final Resource resource = storageUtil.getFileResource(sampleMd5String);
        assertNotNull(resource);
        assertEquals(content.length, resource.contentLength());
    }
}
