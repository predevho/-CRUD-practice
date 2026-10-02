package com.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/greeting")
    public String greeting() {
        return "안녕하세요, Spring Boot!";
    }

    @GetMapping("/number")
    public int number() {
        return 10;
    }

    @GetMapping("/posts/{id}")
    public String post(@PathVariable Long id) {
        return "post id:" + id;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello, " + name;
    }
}
