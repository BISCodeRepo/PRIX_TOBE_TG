package com.prix.livesearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveSearchController {

    @GetMapping("/livesearch")
    public String livesearch() {
        return "/livesearch/livesearchmain";
    }

    @GetMapping("/livesearch/ACTG/actg")
    public String actg() {
        return "/livesearch/ACTG/actg";
    }

    @GetMapping("/livesearch/use")
    public String use() {
        return "/livesearch/use";
    }
}
