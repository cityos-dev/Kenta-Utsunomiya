package com.woven.movie_uploader.filehandler;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private final byte[] contents;

    private FileMetadata(final String fileId, final String name, final int filesize, final String createdAt, final byte[] contents) {
        this.filesize = filesize;
        this.fileId = fileId;
        this.name = name;
        this.createdAt = createdAt;
        this.contents = contents;
    }

    public static class Builder {
        private String fileId;
        private String name;
        private int filesize;
        private String createdAt;
        private byte[] content;

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

        public FileMetadata build() {
            return new FileMetadata(fileId, name, filesize, createdAt, content);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public byte[] getContents(){
        return contents;
    }
}
