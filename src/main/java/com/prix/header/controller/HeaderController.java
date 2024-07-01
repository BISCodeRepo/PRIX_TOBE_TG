package com.prix.header.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HeaderController {

    @GetMapping("/header/login")
    public String login() {
        return "/header/login";
    }

    @GetMapping("/header/help")
    public String help() {
        return "/header/help";
    }

    @GetMapping("/header/contact")
    public String contact() {
        return "/header/contact";
    }

    @GetMapping("/header/admin")
    public String admin() {
        return "/header/admin";
    }

    @GetMapping("/header/newaccount")
    public String newaccount() {
        return "/header/newaccount";
    }

    @GetMapping("/header/registration")
    public String registration() {
        return "/header/registration";
    }
}