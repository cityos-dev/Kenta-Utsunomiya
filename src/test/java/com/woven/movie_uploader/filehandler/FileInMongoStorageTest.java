package com.woven.movie_uploader.filehandler;

import com.woven.movie_uploader.mongo.FileMetadataModel;
import com.woven.movie_uploader.mongo.FileRepository;
import org.bson.codecs.ObjectIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class FileInMongoStorageTest {

    private FileRepository fileRepository;
    private ObjectIdGenerator generator;

    private FileInMongoStorage fileInMongoStorage;

    @BeforeEach
    public void setUp() throws Exception {
        fileRepository = mock(FileRepository.class);
        generator = mock(ObjectIdGenerator.class);
        fileInMongoStorage = new FileInMongoStorage(fileRepository, generator);
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




        when(generator.generate()).thenReturn(testExistQuery);
        when(fileRepository.existsById(eq(testNotExistQuery))).thenReturn(false);
        when(fileRepository.existsById(eq(testExistQuery))).thenReturn(true);
        when(fileRepository.findFileByName(eq(testExistQuery))).thenReturn(model);

        assertFalse(fileInMongoStorage.getFileContents(testNotExistQuery).isPresent()); // check file does not exist

        assertEquals(testExistQuery, fileInMongoStorage.uploadFile(filename, content, contentType)); // check upload success

        final Optional<FileMetadata> metadata = fileInMongoStorage.getFileContents(testExistQuery);
        assertTrue(metadata.isPresent());
        assertEquals(testExistQuery, metadata.get().fileId());
        assertEquals(filename, metadata.get().name());
        assertEquals(contentType, metadata.get().contentType());
        assertEquals(content.length, metadata.get().filesize());


        assertTrue(fileInMongoStorage.deleteFile(testExistQuery));
        assertFalse(fileInMongoStorage.deleteFile(testNotExistQuery));
        verify(fileRepository, times(2)).existsById(eq(testNotExistQuery)); // not exist initially, delete
        verify(fileRepository, times(2)).existsById(eq(testExistQuery)); // not exist initially, delete
        verify(fileRepository, times(1)).insert(any(FileMetadataModel.class));//exist, delete

    }
}
