package com.prix.livesearch.controller;

import com.prix.user.Entity.UserEntity;
import com.prix.user.service.UserService;
import com.prix.user.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LiveSearchController {

    @GetMapping("/livesearch")
    public String livesearch() {
        return "/livesearch/livesearchmain";
    }

    @GetMapping("/livesearch/use")
    public String use() {
        return "/livesearch/use";
    }

    // 로그인 한 경우 username 출력
    @GetMapping("/livesearch/ACTG/actg")
    public String username(Principal principal, Model model) {
        String username = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("username", username);
        return "/livesearch/ACTG/actg";
    }
}
