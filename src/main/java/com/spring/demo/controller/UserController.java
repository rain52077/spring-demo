package com.spring.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")
public class UserController {
    @RequestMapping("/hello")
    public String hello(){
        return "user hello";
    }
}
