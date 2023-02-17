package com.woven.movie_uploader.filehandler;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface FileHandler {
    public boolean deleteFile(final String id) throws IOException;

    public String uploadFile(final String filename, final byte[] content) throws IOException;

    public Resource getFileResource(final String id) throws IOException;

    public Optional<String> getFilenameFromId(final String id);

    public List<FileMetadata> allfiles();
}
