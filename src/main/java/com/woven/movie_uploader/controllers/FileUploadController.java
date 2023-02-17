package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.components.MemoryUtil;
import com.woven.movie_uploader.filehandler.FileHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileUploadController {

    private final FileHandler fileHandler;
    private final String SUCCESS_UPLOAD_MESSAGE = "File uploaded";
    private final String FILE_EXIST_MESSAGE = "File exists";
    private final String UNSUPPORTED_FILE_TYPE_MESSAGE = "Unsupported Media Type";
    private final List<String> supportedMediaTypes = Arrays.asList("video/mp4", "video/mpeg");
    @Autowired
    FileUploadController(final FileHandler storageUtil) {
        this.fileHandler = storageUtil;
    }


    @RequestMapping(method = RequestMethod.POST)
    public synchronized ResponseEntity<String> handleUpload(final MultipartFile data) throws IOException {
        final byte[] byteChar = data.getBytes();
        final String mediaType = data.getContentType();
        if (!supportedMediaTypes.contains(mediaType)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(UNSUPPORTED_FILE_TYPE_MESSAGE);
        }
        final String filename = data.getOriginalFilename();
        final String fileid = fileHandler.uploadFile(filename, byteChar);
        final String response = String.format("v1/files/%s", fileid);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", response).body(SUCCESS_UPLOAD_MESSAGE);
    }
}
