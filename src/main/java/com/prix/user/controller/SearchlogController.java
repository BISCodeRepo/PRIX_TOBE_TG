package com.prix.user.controller;

import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.UserRepository;
import com.prix.user.service.SearchlogService;
import com.prix.user.DTO.SearchlogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchlogController {
    private final SearchlogService searchlogService;
    private final UserRepository userRepository;

    @PostMapping("/actgprocess")
    public String actgPost(SearchlogDTO searchlogDTO) {
        searchlogService.saveSearchlog(searchlogDTO.toEntity());
        return "redirect:/";
    }

    // admin의 searchlog 리스트 출력
    @GetMapping("/admin/searchlog")
    public String searchlogList(Model model) {
        List<SearchlogDTO> searchlogDTOList = searchlogService.getSearchlogList();
        model.addAttribute("searchlogList", searchlogDTOList);
        return "admin/searchlog";
    }

    // 로그인 시 개인 history에 searchlog 리스트 출력
    @GetMapping("/livesearch/history")
    public String history(Principal principal, Model model) {
        Optional<UserEntity> userEntity = userRepository.findByName(principal.getName());
        Integer userID = (principal != null) ? userEntity.get().getId() : 4;
        List<SearchlogDTO> searchlogDTOList = searchlogService.findByUserID(userID);
        log.info("userID: {}", userID);
        model.addAttribute("searchlogList", searchlogDTOList);
        return "/livesearch/history";
    }
}
