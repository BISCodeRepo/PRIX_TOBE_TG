package com.prix.user.controller;

import com.prix.user.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ConfigurationController {
    private final DatabaseService databaseService;


}
