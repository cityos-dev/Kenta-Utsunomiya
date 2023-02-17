package com.woven.movie_uploader.filehandler;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface FileHandler {
    public boolean deleteFile(final String id) throws IOException;

    public String uploadFile(final String filename, final byte[] content, final String contentType) throws IOException;

    public Resource getFileResource(final String id) throws IOException;

    public Optional<FileMetadata> getFileContents(final String id);

    public List<FileMetadata> allfiles();
}
