package com.woven.movie_uploader.mongo;

import com.woven.movie_uploader.filehandler.FileMetadata;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import java.util.Objects;

public record FileMetadataModel(@Id String id, String name, String contentType,
                                String createdAt, Binary content, int size) {
    public FileMetadataModel {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(contentType);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(content);
    }

    public FileMetadata convertToFileMetadata() {
        return new FileMetadata(
                id,name, size, createdAt, contentType
        );
    }
}
