package com.prix.user.controller;

import com.prix.user.DTO.UserDTO;
import com.prix.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        System.out.println("DTO 추가");
        model.addAttribute("userDTO", new UserDTO());
        return "header/registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "header/registration";
        }

        // 비밀번호와 비밀번호 확인이 일치하지 않을 경우
        if (!userDTO.isPasswordEqualToPasswordConfirm()) {
            bindingResult.reject("password", "비밀번호가 일치하지 않습니다.");
            return "header/registration";
        }

        try {
            userService.register(userDTO);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("registrationFailed", "이미 등록된 사용자입니다.");
            return "header/registration";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("registrationFailed", e.getMessage());
            return "header/registration";
        }

        return "redirect:/header/login";
    }

    // 로그인 폼을 보여주는 뷰를 반환
    @GetMapping("/login")
    public String login() {
        return "header/login";
    }

    // 로그아웃 서비스 호출
    @PostMapping("/withdrawl")
    public String userWithdrawl(Model model, Authentication authentication, HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.withdrawl(userDetails.getUsername());
        try {
            request.logout();
        } catch (Exception e) {
            e.printStackTrace();
            // 로그아웃 처리 중 오류가 발생한 경우 처리 로직
        }
        return "redirect:/";
    }

    // 관리자 로그인 페이지를 보여주는 뷰를 반환
    @GetMapping("/adminLogin")
    public String adminLogin() {
        return "header/adminLogin";
    }

    // 로그인 처리
    @PostMapping("/adminLogin")
    public String adminLoginProcess(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    HttpServletRequest request) {
        try {
            request.login(email, password);
            return "redirect:/admin/configuration";
        } catch (ServletException e) {
            // 로그인 실패 처리
            e.printStackTrace();
            return "header/adminLogin";

        }
    }

}