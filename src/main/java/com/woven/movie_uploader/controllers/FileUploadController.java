package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileUploadController {
    private static final Logger LOG = LogManager.getLogger(FileUploadController.class);

    private final FileHandler fileHandler;
    private final String SUCCESS_UPLOAD_MESSAGE = "File uploaded";
    private final String UNSUPPORTED_FILE_TYPE_MESSAGE = "Unsupported Media Type";
    private final Set<String> supportedMediaTypes;

    @Autowired
    FileUploadController(final FileHandler storageUtil,
                         @Value("#{'${allowed.content.types}'.split(',')}") final List<String> allowedContentTypes) {
        this.fileHandler = storageUtil;
        supportedMediaTypes = new HashSet<>(allowedContentTypes);
    }


    @RequestMapping(method = RequestMethod.POST)
    public synchronized ResponseEntity<String> handleUpload(final MultipartFile data) throws IOException {
        final byte[] byteChar = data.getBytes();
        final String mediaType = data.getContentType();
        final String filename = data.getOriginalFilename();
        if (!supportedMediaTypes.contains(mediaType)) {
            LOG.error("User upload the file {} with content type = {} , but this is not in the list of supported media types.", filename, mediaType);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(UNSUPPORTED_FILE_TYPE_MESSAGE);
        }
        LOG.info("Uoloading {}, with media type = {}", filename, mediaType);
        final String fileid = fileHandler.uploadFile(filename, byteChar, mediaType);
        final String response = String.format("v1/files/%s", fileid);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", response).body(SUCCESS_UPLOAD_MESSAGE);
    }
}
