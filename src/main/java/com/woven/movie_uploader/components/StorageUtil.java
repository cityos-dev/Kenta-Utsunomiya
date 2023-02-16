package com.woven.movie_uploader.components;

import com.woven.movie_uploader.filehandler.FileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class StorageUtil implements FileHandler {
    final File rootDir;

    StorageUtil(@Value("${storage.root}") final String rootDir) {
        this.rootDir = new File(rootDir);
    }


    @Override
    public boolean deleteFile(String filename) throws IOException {
        return new File(rootDir, filename).delete();
    }

    @Override
    public boolean exists(String filename) throws IOException {
        return new File(rootDir, filename).exists();
    }

    @Override
    public void uploadFile(String filename) throws IOException {
        Files.createFile(new File(rootDir, filename).toPath());
    }

    @Override
    public Resource getFileResource(String filename) throws IOException {
        final Path path = new File(rootDir, filename).toPath();
        return new PathResource(path);
    }


    @Override
    public List<String> allfiles() {
        return Arrays.stream(rootDir.listFiles()).map(File::getName).collect(Collectors.toList());
    }
}
