package com.woven.movie_uploader.filehandler;

import com.woven.movie_uploader.mongo.FileMetadataModel;
import com.woven.movie_uploader.mongo.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MongoUtilTest {

    private FileRepository fileRepository;
    private MessageDigest digest;
    private FileInMongoHandler fileInMongoHandler;

    private static final String sampleMd5String = "00112233445566778899aabbccddeeff";
    private static final byte[] sampleM5byteArray;

    static {
        sampleM5byteArray = new byte[16];
        for (int i = 0; i < 16; i++) {
            sampleM5byteArray[i] = (byte) (i * 16 + i);
        }
    }


    @BeforeEach
    public void startUp() throws Exception {
        fileRepository = mock(FileRepository.class);
        digest = mock(MessageDigest.class);
        fileInMongoHandler = new FileInMongoHandler(fileRepository, digest);
    }

    @Test
    // mongo connector is auto generated, so this testcase is just for checking the interaction to mongo repository class.
    public void testUpdateExistDelete() throws IOException {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String testNotExistQuery = "aabbcc";
        final String testExistQuery = "ccddaa";
        final FileMetadataModel model = new FileMetadataModel(
                testExistQuery, filename, contentType, new Date().toString(), content
        );
        when(digest.digest(content)).thenReturn(sampleM5byteArray); //
        when(fileRepository.existsById(eq(testNotExistQuery))).thenReturn(false);
        when(fileRepository.existsById(eq(testExistQuery))).thenReturn(true);
        when(fileRepository.findFileByName(eq(testExistQuery))).thenReturn(model);

        assertFalse(fileInMongoHandler.getFileContents(testNotExistQuery).isPresent()); // check file does not exist

        assertEquals(sampleMd5String, fileInMongoHandler.uploadFile(filename, content, contentType)); // check upload success

        final Optional<FileMetadata> metadata = fileInMongoHandler.getFileContents(testExistQuery);
        assertTrue(metadata.isPresent());
        assertEquals(testExistQuery, metadata.get().fileId());
        assertEquals(filename, metadata.get().name());
        assertEquals(contentType, metadata.get().contentType());
        assertEquals(content.length, metadata.get().filesize());


        assertTrue(fileInMongoHandler.deleteFile(testExistQuery));
        assertFalse(fileInMongoHandler.deleteFile(testNotExistQuery));
        verify(fileRepository, times(2)).existsById(eq(testNotExistQuery)); // not exist initially, delete
        verify(fileRepository, times(2)).existsById(eq(testExistQuery)); // not exist initially, delete
        verify(fileRepository, times(1)).insert(any(FileMetadataModel.class));//exist, delete

    }
}
