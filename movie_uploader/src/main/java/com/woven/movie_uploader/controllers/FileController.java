package com.woven.movie_uploader.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/files")
public class FileController {

    final String hogefuga;
    public FileController(@Value("${hoge.fuga}") final String hogefuga) {
        this.hogefuga = hogefuga;
    }
    @RequestMapping(value="/{field}",method = RequestMethod.GET)
    public String download(@PathVariable final String field) {
        return "Download "+ field +" " + hogefuga;
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
