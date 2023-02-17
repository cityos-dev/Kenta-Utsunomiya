package com.woven.movie_uploader.mongo;

import com.woven.movie_uploader.filehandler.FileMetadata;
import org.springframework.data.annotation.Id;

public class FileMetadataModel {
    @Id
    private String id;
    private String name;
    private String contentType;
    private String createdAt;
    private byte[] content;

    public FileMetadataModel(
            final String id,
            final String name,
            final String contentType,
            final String createdAt,
            final byte[] content
    ){
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.createdAt = createdAt;
        this.content = content;
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
