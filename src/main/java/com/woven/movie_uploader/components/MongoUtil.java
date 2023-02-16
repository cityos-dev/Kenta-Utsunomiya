package com.woven.movie_uploader.components;

import com.woven.movie_uploader.filehandler.FileHandler;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public class MongoUtil implements FileHandler {
    @Override
    public boolean deleteFile(String filename) throws IOException {
        return false;
    }

    @Override
    public boolean exists(String filename) throws IOException {
        return false;
    }

    @Override
    public void uploadFile(String filename) throws IOException {

    }

    @Override
    public Resource getFileResource(String filename) throws IOException {
        return null;
    }

    @Override
    public List<String> allfiles() {
        return null;
    }
}
