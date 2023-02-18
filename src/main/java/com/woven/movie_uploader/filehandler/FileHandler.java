package com.woven.movie_uploader.filehandler;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileHandler {
    boolean deleteFile(final String id) throws IOException;

    String uploadFile(final String filename, final byte[] content, final String contentType) throws IOException;

    Resource getFileResource(final String id) throws IOException;

    Optional<FileMetadata> getFileContents(final String id);

    List<FileMetadata> allfiles();
}
