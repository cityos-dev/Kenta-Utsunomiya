package com.woven.movie_uploader.components;

import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.filehandler.FileMetadata;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
// connect with mongo instance
public class MongoUtil implements FileHandler {
    @Override
    public boolean deleteFile(String id) throws IOException {
        return false;
    }

    @Override
    public String uploadFile(String filename, byte[] content) throws IOException {
        return null;
    }

    @Override
    public Resource getFileResource(String id) throws IOException {
        return null;
    }

    @Override
    public Optional<String> getFilenameFromId(String id) {
        return Optional.empty();
    }

    @Override
    public List<FileMetadata> allfiles() {
        return null;
    }
}
