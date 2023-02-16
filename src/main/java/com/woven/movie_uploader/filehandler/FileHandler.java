package com.woven.movie_uploader.filehandler;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileHandler {
    public boolean deleteFile(final String filename) throws IOException;
    public boolean exists(final  String filename) throws IOException;
    public void uploadFile(final  String filename) throws IOException;
    public Resource getFileResource(final String filename) throws IOException;

    public List<String> allfiles();
}
