package com.example.skillsync.controller;

import com.example.skillsync.model.Certificate;
import com.example.skillsync.model.User;
import com.example.skillsync.service.CertificateService;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserService userService;

    @GetMapping("/certificates")
    public String showCertificates(@RequestParam Optional<String> sortBy,
                                   @RequestParam Optional<String> search,
                                   @RequestParam Optional<String> category,
                                   @RequestParam Optional<String> difficulty,
                                   Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Certificate> certificates = certificateService.getCertificatesForUser(user, sortBy, search, category, difficulty);
        model.addAttribute("certificates", certificates);
        return "certificates";
    }
}