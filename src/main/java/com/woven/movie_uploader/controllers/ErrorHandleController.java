package com.woven.movie_uploader.controllers;


import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ErrorHandleController {

    @ExceptionHandler({MultipartException.class, FileUploadException.class})
    public ResponseEntity<String> handleMultipartException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request.");
    }
}
