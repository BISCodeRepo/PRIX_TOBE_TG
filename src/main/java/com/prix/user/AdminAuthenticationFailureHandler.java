package com.prix.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component("adminFailureHandler")
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message = URLEncoder.encode("관리자 로그인 실패: " + exception.getMessage(), StandardCharsets.UTF_8.toString());
        response.sendRedirect("/header/adminLogin?error=" + message);

        String email = request.getParameter("username"); // 폼 입력값
        System.out.println("로그인 실패: 입력한 이메일 = " + email);
        System.out.println("로그인 실패 사유: " + exception.getMessage());

    }
}

