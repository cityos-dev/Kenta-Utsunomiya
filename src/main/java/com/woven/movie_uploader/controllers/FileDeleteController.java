package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.components.MemoryUtil;
import com.woven.movie_uploader.filehandler.FileHandler;
import com.woven.movie_uploader.filehandler.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileDeleteController {

    private final FileHandler fileHandler;
    private static final String SUCCESS_DELETE_RESPONCE_MESSAGE = "File was successfully removed";
    private static final String NOT_FOUND_MESSAGE = "File not found";

    @Autowired
    FileDeleteController(final MemoryUtil storageUtil) {
        this.fileHandler = storageUtil;
    }

    @RequestMapping(value = "/{fileid}", method = RequestMethod.DELETE)
    public ResponseEntity<String> handleDelete(@PathVariable final String fileid) throws IOException, StorageFileNotFoundException {
        final ResponseEntity<String> response;
        if (fileHandler.getFilenameFromId(fileid).isPresent()) {
            fileHandler.deleteFile(fileid);
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).body(SUCCESS_DELETE_RESPONCE_MESSAGE);
        } else {
            throw new StorageFileNotFoundException();
        }
        return response;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleNotfound(final StorageFileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
    }


}
