package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.components.StorageUtil;
import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.filehandler.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileDownloadController {
    private static final String NOT_FOUND_MESSAGE = "File not found";
    final FileHandler fileHandler;

    @Autowired
    FileDownloadController(final StorageUtil storageUtil) {
        this.fileHandler = storageUtil;
    }

    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    public ResponseEntity<Resource> handleDownload(@PathVariable final String filename) throws IOException, StorageFileNotFoundException {
        final ResponseEntity<Resource> response;
        if (fileHandler.exists(filename)) {
            Resource resource = fileHandler.getFileResource(filename);
            response = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", filename))
                    .body(resource);
        } else {
            throw new StorageFileNotFoundException();
        }
        return response;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<String> listFiles() throws IOException {
        return fileHandler.allfiles();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleNotFound(final StorageFileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
    }

}
