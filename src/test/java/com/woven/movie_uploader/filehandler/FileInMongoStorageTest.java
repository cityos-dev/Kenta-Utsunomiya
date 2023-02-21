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
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class FileInMongoStorageTest {

    private FileRepository fileRepository;
    private ObjectIdGenerator generator;
    private Clock clock;
    private final String expectedDate = "2015-02-20T20:31:18Z"; // fixed date

    private FileInMongoStorage fileInMongoStorage;

    @BeforeEach
    public void setUp() throws Exception {
        fileRepository = mock(FileRepository.class);
        generator = mock(ObjectIdGenerator.class);
        final long expectedMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(expectedDate).getTime();
        clock = Clock.fixed(Instant.ofEpochMilli(expectedMillis), ZoneId.of("GMT"));
        fileInMongoStorage = new FileInMongoStorage(fileRepository, generator, clock);
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
                testExistQuery, filename, contentType, expectedDate, content
        );


        when(generator.generate()).thenReturn(testExistQuery);
        when(fileRepository.existsById(eq(testNotExistQuery))).thenReturn(false);
        when(fileRepository.existsById(eq(testExistQuery))).thenReturn(true);
        when(fileRepository.findFileByName(eq(testExistQuery))).thenReturn(model);

        assertFalse(fileInMongoStorage.getFileContents(testNotExistQuery).isPresent()); // check file does not exist

        assertEquals(testExistQuery, fileInMongoStorage.uploadFile(filename, content, contentType)); // check upload success

        final Optional<FileMetadata> metadata = fileInMongoStorage.getFileContents(testExistQuery);
        assertTrue(metadata.isPresent());


        assertTrue(fileInMongoStorage.deleteFile(testExistQuery));
        assertFalse(fileInMongoStorage.deleteFile(testNotExistQuery));
        verify(fileRepository, times(2)).existsById(eq(testNotExistQuery)); // not exist initially, delete
        verify(fileRepository, times(2)).existsById(eq(testExistQuery)); // not exist initially, delete
        verify(fileRepository, times(1)).insert(eq(model));// check what's insert

    }

    @Test
    public void testInsert() throws Exception {
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String objectId = "objectid";
        when(generator.generate()).thenReturn(objectId);
        fileInMongoStorage.uploadFile(filename, content, contentType);
        final FileMetadataModel model = new FileMetadataModel(
                objectId, filename, contentType, expectedDate, content
        );
        verify(generator, times(1)).generate();
        verify(fileRepository, times(1)).insert(eq(model));
    }

    @Test
    public void testFindExist() throws Exception {
        final String objectid = "objectid";
        final String filename = "file1";
        final byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        final String contentType = "contentType";
        final String objectId = "objectid";
        final FileMetadataModel model = new FileMetadataModel(
                objectId, filename, contentType, expectedDate, content
        );
        when(fileRepository.existsById(eq(objectId))).thenReturn(true);
        when(fileRepository.findFileByName(eq(objectId))).thenReturn(model);
        final Optional<FileMetadata> metadata = fileInMongoStorage.getFileContents(objectid);
        assertTrue(metadata.isPresent());
        assertEquals(model.convertToFileMetadata(), metadata.get());
        verify(fileRepository, times(1)).existsById(eq(objectId));
        verify(fileRepository, times(1)).findFileByName(eq(objectId));
    }

    @Test
    public void testNotFound() throws Exception {
        final String objectid = "objectid";
        when(fileRepository.existsById(eq(objectid))).thenReturn(false);
        final Optional<FileMetadata> metadata = fileInMongoStorage.getFileContents(objectid);
        assertFalse(metadata.isPresent());
        verify(fileRepository, times(1)).existsById(eq(objectid));
        verify(fileRepository, never()).findFileByName(eq(objectid));
    }

    @Test
    public void testDeleteExist() throws Exception {
        final String id = "exist";
        when(fileRepository.existsById(eq(id))).thenReturn(true);
        assertTrue(fileInMongoStorage.deleteFile(id));
        verify(fileRepository, times(1)).existsById(eq(id));
        verify(fileRepository, times(1)).deleteById(eq(id));
    }

    @Test
    public void testDeleteNotExist() throws Exception {
        final String id = "notexist";
        when(fileRepository.existsById(eq(id))).thenReturn(false);
        assertFalse(fileInMongoStorage.deleteFile(id));
        verify(fileRepository, times(1)).existsById(eq(id));
        verify(fileRepository, never()).deleteById(eq(id));
        ;
    }

    @Test
    public void testList() throws Exception {
        final FileMetadataModel model1 = new FileMetadataModel(
                "id1",
                "fil1.mpeg",
                "video/mpeg",
                "2023-01-03T00:11:22Z",
                "content1".getBytes(StandardCharsets.UTF_8)
        );
        final FileMetadataModel model2 = new FileMetadataModel(
                "id2",
                "fil2.mp4",
                "video/mp4",
                "2027-01-03T00:11:22Z",
                "content2".getBytes(StandardCharsets.UTF_8)
        );
        final List<FileMetadataModel> modelList = Arrays.asList(model1, model2);
        when(fileRepository.findAll()).thenReturn(modelList);
        assertIterableEquals(
                modelList.stream().map(FileMetadataModel::convertToFileMetadata).collect(Collectors.toList()),
                fileInMongoStorage.allfiles()
        );
        verify(fileRepository, times(1)).findAll();
    }
}
