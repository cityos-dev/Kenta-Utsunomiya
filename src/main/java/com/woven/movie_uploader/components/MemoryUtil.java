package com.woven.movie_uploader.components;

import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.filehandler.FileMetadata;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
//connect with memory instance
public class MemoryUtil implements FileHandler {

    private final Map<String, FileMetadata> fileMetadataMap = new HashMap<>();
    private final MessageDigest md5;

    public MemoryUtil() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");
    }


    @Override
    public synchronized boolean deleteFile(String id) throws IOException {
        return Optional.ofNullable(fileMetadataMap.remove(id)).isPresent();
    }


    @Override
    public synchronized String uploadFile(final String filename, final byte[] content) throws IOException {
        final String fileid = MD5Encoder.encode(md5.digest(content));
        final FileMetadata fileMetadata = FileMetadata.builder()
                .setFileId(fileid)
                .setName(filename)
                .setFilesize(content.length)
                .setCreatedAt(new Date().toString())
                .setContent(content)
                .build();
        fileMetadataMap.put(fileid, fileMetadata);

        return fileid;
    }

    @Override
    public Resource getFileResource(String id) throws IOException {
        return new ByteArrayResource(fileMetadataMap.get(id).getContents());
    }

    @Override
    public Optional<String> getFilenameFromId(String id) {
        if (fileMetadataMap.containsKey(id)) {
            return Optional.ofNullable(fileMetadataMap.get(id).getName());
        } else {
            return Optional.empty();
        }
    }


    @Override
    public List<FileMetadata> allfiles() {
        return fileMetadataMap.values().stream().toList();
    }
}
