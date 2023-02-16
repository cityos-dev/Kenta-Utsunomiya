package com.woven.movie_uploader.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/${api.version}/files")
public class FileUploadController {
    @RequestMapping(method = RequestMethod.POST)
    public String handleUpload() {
        return "Post fuga";
    }

}
