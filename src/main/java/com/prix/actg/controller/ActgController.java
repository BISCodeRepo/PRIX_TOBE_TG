package com.prix.actg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ActgController {

    @GetMapping("/actghelp")
    public String actghelp() {
        return "livesearch/ACTG/actghelp";
    }

    @GetMapping("/livesearch/ACTG/actgprocess")
    public String actgprocess() {
        return "livesearch/ACTG/actgprocess";
    }

}
