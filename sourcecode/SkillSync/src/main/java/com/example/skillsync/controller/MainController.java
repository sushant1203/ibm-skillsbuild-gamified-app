package com.example.skillsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String landing() {
        return "landing";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

}

