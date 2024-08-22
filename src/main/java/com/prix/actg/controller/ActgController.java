package com.prix.actg.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 컨트롤러
@Controller
public class ActgController {

    @GetMapping("/actghelp")
    public String actghelp() {
        return "livesearch/ACTG/actghelp";
    }

    @GetMapping("/livesearch/ACTG/actgprocess")
    public String actgprocess() {
        return "livesearch/ACTG/actgprocess";
    }

    @Autowired
    private DataSource dataSource; // 데이터소스는 Spring에서 자동으로 주입

//    @GetMapping("/actgresult")
//    public String actgresult(Model model, HttpSession session, @RequestParam String index) {
//        try (Connection conn = dataSource.getConnection()) {
//            if (conn == null) {
//                return "redirect:/login.jsp?url=ACTG/result.jsp";
//            }
//
//            String id = (String) session.getAttribute("id");
//            if (id == null) {
//                session.setAttribute("id", "4");
//                id = (String) session.getAttribute("id");
//            }
//
//            Statement state = conn.createStatement();
//            ResultSet rs = null;
//
//            String userName = null;
//            String title = null;
//
//            // 데이터베이스에서 데이터를 가져오는 로직...
//            // ...
//
//            // 모델에 데이터를 추가
//            model.addAttribute("userName", userName);
//            model.addAttribute("title", title);
//            // ...
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return "actgresult"; // 뷰 이름 반환
//    }
}
