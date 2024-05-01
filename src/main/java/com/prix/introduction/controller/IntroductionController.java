package com.prix.introduction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroductionController {
    @GetMapping("/")
    public String introduction() {
        return "introduction";
    }
}
