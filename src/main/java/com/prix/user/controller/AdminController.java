package com.prix.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    // 관리자 페이지를 보여주는 뷰를 반환
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/configuration")
//    public String configuration() {
//        return "admin/configuration";
//    }
}
