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

}
