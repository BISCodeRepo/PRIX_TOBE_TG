package com.prix.user.controller;

import com.prix.user.DTO.SearchlogDTO;
import com.prix.user.SearchlogEntity;
import com.prix.user.service.SearchlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchlogController {
    private final SearchlogService searchlogService;

    @PostMapping("/actgprocess")
    public String actgPost(SearchlogDTO searchlogDTO) {
        searchlogService.saveSearchlog(searchlogDTO.toEntity());
        // 클라이언트를 루트 URL(/)로 리다이렉트합니다.
        return "redirect:/";
    }

    @GetMapping("/admin/searchlog")
    public String list(Model model) {
        List<SearchlogDTO> searchlogDTOList = searchlogService.getSearchlogList();
        model.addAttribute("searchlogList", searchlogDTOList);
        return "admin/searchlog";
    }
}
