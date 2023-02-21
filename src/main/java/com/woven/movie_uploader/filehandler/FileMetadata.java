package com.woven.movie_uploader.filehandler;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

// Metadata file that can be converted to JSON.
public record FileMetadata(@JsonProperty("fileid") String fileId, @JsonProperty("name") String name,
                           @JsonProperty("size") long filesize, @JsonProperty("created_at") String createdAt,
                           @JsonIgnore String contentType) {

    public FileMetadata {
        Objects.requireNonNull(fileId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(contentType);
    }
}
