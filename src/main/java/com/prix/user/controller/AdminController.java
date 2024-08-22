package com.prix.user.controller;

import com.prix.user.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DatabaseService database;

    @GetMapping("/searchlog")
    public String searchlog() {
        return "admin/searchlog";
    }

//    @PostMapping("/update")
//    public ResponseEntity<String> updateValue(@RequestBody String value) {
//        // 데이터베이스 업데이트
//        String newRowValue = database.update(value);  // database.update는 데이터베이스를 업데이트하는 메서드
//
//        // 새로운 테이블 행 데이터 반환
//        return new ResponseEntity<>(newRowValue, HttpStatus.OK);
//    }

    // 관리자 페이지를 보여주는 뷰를 반환
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/configuration")
    public String configuration() {
        return "admin/configuration";
    }
}
