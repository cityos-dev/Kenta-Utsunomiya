package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.components.MemoryUtil;
import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.filehandler.FileMetadata;
import com.woven.movie_uploader.filehandler.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileDownloadController {
    private static final String NOT_FOUND_MESSAGE = "File not found";
    final FileHandler fileHandler;

    @Autowired
    FileDownloadController(final FileHandler storageUtil) {
        this.fileHandler = storageUtil;
    }

    @RequestMapping(value = "/{fileid}", method = RequestMethod.GET)
    public ResponseEntity<Resource> handleDownload(@PathVariable final String fileid) throws IOException {
        final ResponseEntity<Resource> response;
        final Optional<String> filenameOptional = fileHandler.getFilenameFromId(fileid);
        if (filenameOptional.isPresent()) {
            Resource resource = fileHandler.getFileResource(fileid);
            final String filename = filenameOptional.get();
            final String ContentType;
            if (filename.endsWith(".mp4")) {
                ContentType = "video/mp4";
            } else {
                ContentType = "video/mpeg";
            }
            response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, ContentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", filename))
                    .body(resource);
        } else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ByteArrayResource(NOT_FOUND_MESSAGE.getBytes(StandardCharsets.UTF_8))
            );
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FileMetadata> listFiles() {
        return fileHandler.allfiles();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleNotFound(final StorageFileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
    }

}
