package com.woven.movie_uploader.controllers;

import com.woven.movie_uploader.filehandler.FileStorage;
import com.woven.movie_uploader.filehandler.FileMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/files")
public class FileDownloadController {
    private static final Logger LOG = LogManager.getLogger(FileDownloadController.class);
    private static final String NOT_FOUND_MESSAGE = "File not found";
    final FileStorage fileStorage;

    @Autowired
    FileDownloadController(final FileStorage storageUtil) {
        this.fileStorage = storageUtil;
    }

    @RequestMapping(value = "/{fileid}", method = RequestMethod.GET)
    public ResponseEntity<Resource> handleDownload(@PathVariable final String fileid) throws IOException {
        final ResponseEntity<Resource> response;
        final Optional<FileMetadata> metadataOptional = fileStorage.getFileContents(fileid);
        if (metadataOptional.isPresent()) {
            final FileMetadata fileMetadata = metadataOptional.get();
            final Resource resource = new ByteArrayResource(fileMetadata.contents());
            final String filename = fileMetadata.name();
            final String contentType = fileMetadata.contentType();
            LOG.info("File found: fileid = {}, filename = {}, content type = {} ", fileid, filename, contentType);

            response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", filename))
                    .body(resource);
        } else {
            LOG.warn("File cannot be found: fileid = {}", fileid);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ByteArrayResource(NOT_FOUND_MESSAGE.getBytes(StandardCharsets.UTF_8))
            );
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FileMetadata> listFiles() {
        return fileStorage.allfiles();
    }

}
