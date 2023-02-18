package com.woven.movie_uploader.filehandler;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//connect with memory instance
public class FileInMemoryHandler implements FileHandler {

    private final Map<String, FileMetadata> fileMetadataMap = new HashMap<>();
    private final MessageDigest md5 ;

    public FileInMemoryHandler(final MessageDigest md5) {
        this.md5 = md5;
    }


    @Override
    public synchronized boolean deleteFile(String id) throws IOException {
        return Optional.ofNullable(fileMetadataMap.remove(id)).isPresent();
    }


    @Override
    public synchronized String uploadFile(final String filename, final byte[] content, final String contentType) throws IOException {
        final String fileid = MD5Encoder.encode(md5.digest(content));
        final FileMetadata fileMetadata = FileMetadata.builder()
                .setFileId(fileid)
                .setName(filename)
                .setFilesize(content.length)
                .setCreatedAt(new Date().toString())
                .setContent(content)
                .setContentType(contentType)
                .build();
        fileMetadataMap.put(fileid, fileMetadata);

        return fileid;
    }

    @Override
    public Resource getFileResource(String id) throws IOException {
        return new ByteArrayResource(fileMetadataMap.get(id).getContents());
    }

    @Override
    public Optional<FileMetadata> getFileContents(String id) {
        return Optional.ofNullable(fileMetadataMap.get(id));
    }


    @Override
    public List<FileMetadata> allfiles() {
        return fileMetadataMap.values().stream().toList();
    }
}
