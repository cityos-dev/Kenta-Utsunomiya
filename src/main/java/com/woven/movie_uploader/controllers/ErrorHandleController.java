package com.woven.movie_uploader.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
/*
 This ControllerAdvice is for handling exception when the request header is Multipart, but it does not have boundary.
 */
@ControllerAdvice
public class ErrorHandleController {

    private static final Logger LOG = LogManager.getLogger(ErrorHandleController.class);

    @ExceptionHandler({MultipartException.class, FileUploadException.class, NullPointerException.class})
    public ResponseEntity<String> handleMultipartException(final Exception e) {
        LOG.error("An Fatal Error occurred, on the client request.", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request.");
    }
}
