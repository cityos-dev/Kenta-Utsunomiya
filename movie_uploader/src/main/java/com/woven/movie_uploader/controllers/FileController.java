package com.woven.movie_uploader.controllers;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
@RequestMapping(value="/files")
public class FileController {
    @RequestMapping(value="/{field}",method = RequestMethod.GET)
    public String download(@PathVariable final String field) {
        return "Download "+ field;
    }
    @RequestMapping(value="/{field}",method = RequestMethod.DELETE)
    public String delete(@PathVariable final String field) {
        return "Delete" + field;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post() {
        return "Post";
    }
}
