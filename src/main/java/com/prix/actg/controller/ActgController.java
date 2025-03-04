package com.prix.actg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class ActgController {

    // actghelp 페이지
    @GetMapping("/actghelp")
    public String actghelp() {
        return "livesearch/ACTG/actghelp";
    }

    // 예비용 actgprocess 페이지
    @GetMapping("/livesearch/ACTG/actgprocess")
    public String actgprocess() {
        return "livesearch/ACTG/actgprocess";
    }
}
