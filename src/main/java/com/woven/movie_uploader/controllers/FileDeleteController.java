package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/v1/files")
public class FileDeleteController {
    private static final Logger LOG = LogManager.getLogger(FileDeleteController.class);

    private final FileStorage fileStorage;
    private static final String SUCCESS_DELETE_RESPONCE_MESSAGE = "File was successfully removed";
    private static final String NOT_FOUND_MESSAGE = "File not found";

    @Autowired
    FileDeleteController(final FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @RequestMapping(value = "/{fileid}", method = RequestMethod.DELETE)
    public ResponseEntity<String> handleDelete(@PathVariable final String fileid) throws IOException {
        if (fileStorage.deleteFile(fileid)) {
            LOG.info("Delete existing file: fileid = {}", fileid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(SUCCESS_DELETE_RESPONCE_MESSAGE);
        } else {
            LOG.warn("File cannot be found: fileid = {}", fileid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
        }
    }
}
