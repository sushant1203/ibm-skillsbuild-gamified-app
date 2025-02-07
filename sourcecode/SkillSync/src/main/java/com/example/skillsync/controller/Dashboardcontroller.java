package com.example.skillsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class Dashboardcontroller {
    @GetMapping("/")
    public String getDashboardpage(){
        return "dashboard";
    }
}
