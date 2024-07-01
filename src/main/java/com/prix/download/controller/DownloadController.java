package com.prix.download.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DownloadController {
    @GetMapping("/download")
    public String download() {
        return "/download/modplus";
    }

    @GetMapping("/download/actg")
    public String actg() {
        return "/download/actg";
    }

    @GetMapping("/download/cifter")
    public String cifter() {
        return "/download/cifter";
    }

    @GetMapping("/download/dbond")
    public String dbond() {
        return "/download/dbond";
    }

    @GetMapping("/download/demix")
    public String deMix() {
        return "/download/demix";
    }

    @GetMapping("/download/moda")
    public String moda() {
        return "download/moda";
    }

    @GetMapping("/download/modplus")
    public String modplus() {
        return "download/modplus";
    }

    @GetMapping("/download/DDPSearch")
    public String ddpsearch() {
        return "download/ddpsearch";
    }

    @GetMapping("/download/nextsearch")
    public String nextsearch() {
        return "download/nextsearch";
    }

    @GetMapping("/download/mutcombinator")
    public String mutcombinator() {
        return "download/mutcombinator";
    }
}