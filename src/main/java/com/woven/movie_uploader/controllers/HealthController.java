package com.woven.movie_uploader.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1" )
public class HealthController {
    @GetMapping("/health")
    public String getHealth() {
        return "OK";
    }
}
