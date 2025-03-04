package com.prix.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    // 로그인 실패 시 로그 출력을 위한 메소드
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패했습니다.";
        }
        // 오류 메시지를 URL 인코딩
        errorMessage = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8.toString());
        // 로그인 페이지로 리다이렉트하고, 오류 메시지를 쿼리 파라미터로 추가
        response.sendRedirect("/user/login?error=" + errorMessage);
    }
}
