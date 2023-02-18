package com.woven.movie_uploader.mongo;

import com.woven.movie_uploader.filehandler.FileMetadata;
import org.springframework.data.annotation.Id;

import java.util.Objects;

public record FileMetadataModel(@Id String id, String name, String contentType,
                                String createdAt, byte[] content) {

    public FileMetadataModel {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(contentType);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(content);
    }

    public FileMetadata convertToFileMetadata() {
        return FileMetadata.builder()
                .setFileId(id)
                .setContentType(contentType)
                .setContent(content)
                .setCreatedAt(createdAt)
                .setName(name)
                .setFilesize(content.length)
                .build();
    }
}
