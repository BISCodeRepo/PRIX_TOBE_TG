package com.prix.download.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DownloadController {
    @GetMapping("/download")
    public String download() {
        return "redirect:/download/modplus";
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
