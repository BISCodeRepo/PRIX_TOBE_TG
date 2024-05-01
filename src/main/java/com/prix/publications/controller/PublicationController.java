package com.prix.publications.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicationController {
    @GetMapping("/publications")
    public String publications() {
        return "publications";
    }
}
