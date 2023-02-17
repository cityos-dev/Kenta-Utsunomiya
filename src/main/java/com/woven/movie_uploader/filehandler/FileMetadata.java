package com.woven.movie_uploader.filehandler;


import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetadata {

    @JsonProperty("fileid")
    private final String fileId;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("size")
    private final int filesize;
    @JsonProperty("created_at")
    private final String createdAt;

    private FileMetadata(final String fileId, final String name, final int filesize, final String createdAt) {
        this.filesize = filesize;
        this.fileId = fileId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static class Builder {
        private String fileId;
        private String name;
        private int filesize;
        private String createdAt;

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

        public FileMetadata build() {
            return new FileMetadata(fileId, name, filesize, createdAt);
        }

    }

    public static Builder builder() {
        return new Builder();
    }
}
