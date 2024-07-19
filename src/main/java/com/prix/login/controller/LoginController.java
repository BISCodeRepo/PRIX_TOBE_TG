package com.prix.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @PostMapping("/perform_login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // 로그인 서비스 호출
        // 예: loginService.login(username, password);
        return "redirect:/"; // 로그인 성공 후 리다이렉트할 페이지
    }

    @PostMapping("/perform_newaccount")
    public String newaccount(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        // 회원가입 서비스 호출
        // 예: registerService.register(username, password, email);
        return "redirect:/login"; // 회원가입 성공 후 리다이렉트할 페이지
    }

    @GetMapping("/perform_logout")
    public String logout() {
        // 로그아웃 서비스 호출
        // 예: logoutService.logout();
        return "redirect:/login"; // 로그아웃 성공 후 리다이렉트할 페이지
    }

    @GetMapping("/login")
    public String showLoginForm() {
        // 로그인 폼을 보여주는 뷰를 반환
        return "/header/login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        // 회원가입 폼을 보여주는 뷰를 반환
        return "/header/registration";
    }

    @GetMapping("/newaccount")
    public String showNewAccountForm() {
        // 새 계정 생성 폼을 보여주는 뷰를 반환
        return "/header/newaccount";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        // 관리자 페이지를 보여주는 뷰를 반환
        return "/header/admin";
    }
}