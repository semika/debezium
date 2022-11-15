package com.aeturnum.mosaic.cdc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
