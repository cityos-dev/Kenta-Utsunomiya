package com.woven.movie_uploader.filehandler;

import org.bson.codecs.ObjectIdGenerator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// The initial implementation of file storage. Save every data on memory, but will not be used after migrating tp mongo.
public class FileInMemoryStorage implements FileStorage {

    private final Map<String, FileMetadata> fileMetadataMap = new HashMap<>();
    private final ObjectIdGenerator objectIdGenerator;
    private final Clock clock;

    public FileInMemoryStorage(final ObjectIdGenerator objectIdGenerator,
                               final Clock clock) {
        this.objectIdGenerator = objectIdGenerator;
        this.clock = clock;
    }


    @Override
    public synchronized boolean deleteFile(String id) throws IOException {
        return Optional.ofNullable(fileMetadataMap.remove(id)).isPresent();
    }


    @Override
    public synchronized String uploadFile(final String filename, final byte[] content, final String contentType) throws IOException {
        final String fileid = objectIdGenerator.generate().toString();
        final FileMetadata fileMetadata = FileMetadata.builder()
                .setFileId(fileid)
                .setName(filename)
                .setFilesize(content.length)
                .setCreatedAt(Instant.now(clock).toString())
                .setContent(content)
                .setContentType(contentType)
                .build();
        fileMetadataMap.put(fileid, fileMetadata);

        return fileid;
    }

    @Override
    public Resource getFileResource(final String id) throws IOException {
        return new ByteArrayResource(fileMetadataMap.get(id).contents());
    }

    @Override
    public Optional<FileMetadata> getFileContents(final String id) {
        return Optional.ofNullable(fileMetadataMap.get(id));
    }


    @Override
    public List<FileMetadata> allfiles() {
        return fileMetadataMap.values().stream().toList();
    }
}
