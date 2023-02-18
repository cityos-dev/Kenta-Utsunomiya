package com.woven.movie_uploader.filehandler;

import com.woven.movie_uploader.mongo.FileMetadataModel;
import com.woven.movie_uploader.mongo.FileRepository;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// connect with mongo instance

public class FileInMongoHandler implements FileHandler {
    final FileRepository mongoFileRepository;
    final MessageDigest messageDigest;

    public FileInMongoHandler(final FileRepository repository, final MessageDigest digest) {
        this.mongoFileRepository = repository;
        this.messageDigest = digest;
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
    public String uploadFile(final String filename, byte[] content, final String contentType) throws IOException {
        final String id = MD5Encoder.encode(messageDigest.digest(content));
        final FileMetadataModel model = new FileMetadataModel(
                id,
                filename,
                contentType,
                new Date().toString(), // TODO make it configurable from testing.
                content
        );
        mongoFileRepository.insert(model);
        return id;
    }

    @Override
    public Resource getFileResource(String id) throws IOException {
        final FileMetadataModel model = mongoFileRepository.findFileByName(id);
        return new ByteArrayResource(model.convertToFileMetadata().contents());
    }

    @Override
    public Optional<FileMetadata> getFileContents(String id) {
        if (mongoFileRepository.existsById(id)) {
            return Optional.of(
                    mongoFileRepository.findFileByName(id).convertToFileMetadata()
            );
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
