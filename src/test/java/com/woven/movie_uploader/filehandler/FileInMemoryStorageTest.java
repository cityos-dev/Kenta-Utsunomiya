package com.woven.movie_uploader.filehandler;

import org.bson.codecs.ObjectIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileInMemoryStorageTest {

    private FileInMemoryStorage storageUtil;
    private ObjectIdGenerator objectIdGenerator;
    private Clock clock;
    private final String expectedDate = "2023-02-20T20:31:18Z"; // fixed date


    @BeforeEach
    public void startUp() throws Exception {
        final long expectedMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(expectedDate).getTime();
        clock = Clock.fixed(Instant.ofEpochMilli(expectedMillis), ZoneId.of("GMT"));
        objectIdGenerator = mock(ObjectIdGenerator.class);
        storageUtil = new FileInMemoryStorage(objectIdGenerator, clock);
    }

    @Test
    public void testUpdateExistDelete() throws Exception {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String id = "aabbcc";
        when(objectIdGenerator.generate()).thenReturn(id);
        assertEquals(id, storageUtil.uploadFile(filename, content, contentType));
        // upload
        final Optional<FileMetadata> fileContent = storageUtil.getFileContents(id);
        assertTrue(fileContent.isPresent());
        final FileMetadata expectedMetadata = FileMetadata.builder()
                .setFileId(id)
                .setName(filename)
                .setFilesize(content.length)
                .setContent(content)
                .setContentType(contentType)
                .setCreatedAt(expectedDate).build();
        assertEquals(expectedMetadata, fileContent.get());

        // delete file
        assertTrue(storageUtil.deleteFile(id));

        // delete that does not exist.
        assertFalse(storageUtil.getFileContents(id).isPresent());
    }

}
