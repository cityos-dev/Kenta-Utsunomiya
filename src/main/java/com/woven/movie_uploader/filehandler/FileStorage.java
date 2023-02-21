package com.woven.movie_uploader.filehandler;

import org.springframework.data.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

// Abstract interface for FileStorage.
public interface FileStorage {
    boolean deleteFile(final String id) throws IOException;

    String uploadFile(final String filename, final InputStream inputStream, final String contentType) throws IOException;

    Optional<Pair<FileMetadata, InputStream>> getFileContents(final String id);

    List<FileMetadata> allfiles();
}
