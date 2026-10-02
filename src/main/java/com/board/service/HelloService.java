package com.board.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String createGreeting(String name) {
        return "Hello, " + name;

    }
}
