package com.sqlinjection.sqlinjectiondemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {



    @PostMapping("/input")
    public String testController (@RequestBody String inputString){

         return " test attempted";

    }
}
