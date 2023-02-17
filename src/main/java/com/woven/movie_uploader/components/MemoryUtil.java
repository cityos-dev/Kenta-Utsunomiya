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
public class MemoryUtil implements FileHandler {

    private final Map<String, byte[]> idToContent = new HashMap<>();
    private final Map<String, String> idToFilename = new HashMap<>();
    private final Map<String, FileMetadata> fileMetadataMap = new HashMap<>();
    private final MessageDigest md5;

    MemoryUtil() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");
    }


    @Override
    public synchronized boolean deleteFile(String id) throws IOException {
        if (idToContent.containsKey(id)) {
            idToContent.remove(id);
            idToFilename.remove(id);
            fileMetadataMap.remove(id);
            // should be atomic
        }
        return false;
    }


    @Override
    public synchronized String uploadFile(final String filename, final byte[] content) throws IOException {
        final String fileid = MD5Encoder.encode(md5.digest(content));
        final FileMetadata fileMetadata = FileMetadata.builder()
                .setFileId(fileid)
                .setName(filename)
                .setFilesize(content.length)
                .setCreatedAt(new Date().toString())
                .build();

        idToContent.put(fileid, content);
        idToFilename.put(fileid, filename);
        fileMetadataMap.put(fileid, fileMetadata);

        return fileid;
    }

    @Override
    public Resource getFileResource(String id) throws IOException {
        return new ByteArrayResource(idToContent.get(id));
    }

    @Override
    public Optional<String> getFilenameFromId(String id) {
        return Optional.ofNullable(idToFilename.get(id));
    }


    @Override
    public List<FileMetadata> allfiles() {
        return fileMetadataMap.values().stream().toList();
    }
}
