package com.woven.movie_uploader.filehandler;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

// Metadata file that can be converted to JSON.
public record FileMetadata(@JsonProperty("fileid") String fileId, @JsonProperty("name") String name,
                           @JsonProperty("size") int filesize, @JsonProperty("created_at") String createdAt,
                           @JsonIgnore byte[] contents, @JsonIgnore String contentType) {

    public FileMetadata {
        Objects.requireNonNull(fileId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(contents);
        Objects.requireNonNull(contentType);
    }

    public static class Builder {
        private String fileId;
        private String name;
        private int filesize;
        private String createdAt;
        private byte[] content;
        private String contentType;

        public Builder setFileId(final String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setFilesize(final int filesize) {
            this.filesize = filesize;
            return this;
        }

        public Builder setCreatedAt(final String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setContent(final byte[] content) {
            this.content = content;
            return this;
        }

        public Builder setContentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public FileMetadata build() {
            return new FileMetadata(fileId, name, filesize, createdAt, content, contentType);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}
