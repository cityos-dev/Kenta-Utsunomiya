package com.woven.movie_uploader.filehandler;

import org.bson.codecs.ObjectIdGenerator;
import org.springframework.data.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// The initial implementation of file storage. Save every data on memory, but will not be used after migrating tp mongo.
public class FileInMemoryStorage implements FileStorage {

    private final Map<String, Pair<FileMetadata, byte[]>> fileMetadataMap = new HashMap<>();
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
    public synchronized String uploadFile(final String filename, final InputStream inputStream, final String contentType) throws IOException {
        final String fileid = objectIdGenerator.generate().toString();
        final byte[] content = inputStream.readAllBytes();
        fileMetadataMap.put(fileid, Pair.of(
                new FileMetadata(fileid, filename, content.length, Instant.now(clock).toString(), contentType),
                content)
        );

        return fileid;
    }

    @Override
    public Optional<Pair<FileMetadata, InputStream>> getFileContents(final String id) {
        return Optional.ofNullable(fileMetadataMap.get(id)).map(elem -> Pair.of(elem.getFirst(), new ByteArrayInputStream(elem.getSecond())));
    }

    @Override
    public List<FileMetadata> allfiles() {
        return fileMetadataMap.values().stream().map(Pair::getFirst).collect(Collectors.toList());
    }
}
