package com.example.lab5_s2_rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;



@Controller
public class WebController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String DOG_API_URL = "https://dog.ceo/api/breeds/image/random";

    @GetMapping("/getRandomDog")
    public String getRandomDog(Model model) {

        String response = restTemplate.getForObject(DOG_API_URL, String.class);
        String dogImageUrl = response.split("\"message\":\"")[1].split("\"")[0];
        model.addAttribute("dogImageUrl", dogImageUrl);
        model.addAttribute("message","heres a random dog for you");

        return "result";
    }









}
