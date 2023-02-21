package com.woven.movie_uploader.filehandler;

import com.woven.movie_uploader.mongo.FileMetadataModel;
import com.woven.movie_uploader.mongo.FileRepository;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.Binary;
import org.springframework.data.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementation of FileStorage for Mongo DB

public class FileInMongoStorage implements FileStorage {
    private final FileRepository mongoFileRepository;
    private final ObjectIdGenerator objectIdGenerator;
    private final Clock clock;

    public FileInMongoStorage(final FileRepository repository,
                              final ObjectIdGenerator objectIdGenerator,
                              final Clock clock) {
        this.mongoFileRepository = repository;
        this.objectIdGenerator = objectIdGenerator;
        this.clock = clock;
    }

    @Override
    public boolean deleteFile(final String id) throws IOException {
        if (mongoFileRepository.existsById(id)) {
            mongoFileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public String uploadFile(final String filename, final InputStream inputStream, final String contentType) throws IOException {
        final String id = objectIdGenerator.generate().toString();
        final Binary binary = new Binary(inputStream.readAllBytes());
        final FileMetadataModel model = new FileMetadataModel(
                id,
                filename,
                contentType,
                Instant.now(clock).toString(),
                binary,
                binary.length()
        );
        mongoFileRepository.insert(model);
        return id;
    }

    @Override
    public Optional<Pair<FileMetadata, InputStream>> getFileContents(final String id) {
        if (mongoFileRepository.existsById(id)) {
            final FileMetadataModel model = mongoFileRepository.findFileByName(id);
            return Optional.of(Pair.of(model.convertToFileMetadata(), new ByteArrayInputStream(model.content().getData())));
        } else {
            return Optional.empty();
        }
    }


    @Override
    public List<FileMetadata> allfiles() {
        return mongoFileRepository.findAll().stream().map(
                FileMetadataModel::convertToFileMetadata
        ).collect(Collectors.toList());
    }
}
