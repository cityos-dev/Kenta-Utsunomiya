package com.woven.movie_uploader.filehandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Abstract interface for FileStorage.
public interface FileStorage {
    boolean deleteFile(final String id) throws IOException;

    String uploadFile(final String filename, final byte[] content, final String contentType) throws IOException;

    Optional<FileMetadata> getFileContents(final String id);

    List<FileMetadata> allfiles();
}
