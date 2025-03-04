package com.prix.header.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HeaderController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HeaderController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/header/login")
    public String login() {
        return "header/login";
    }


    @GetMapping("/header/help")
    public String help() {
        return "header/help";
    }

    @GetMapping("/header/contact")
    public String contact() {
        return "header/contact";
    }

    @GetMapping("/header/adminLogin")
    public String admin() {
        return "header/adminLogin";
    }

    @GetMapping("/header/registration")
    public String registration() {
        return "header/registration";
    }

    @GetMapping("/header/terms")
    public String terms() {
        return "header/terms";
    }
}