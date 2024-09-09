package com.prix.user.controller;

import com.prix.user.DTO.DatabaseDTO;
import com.prix.user.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConfigurationController {
    private final DatabaseService databaseService;

    @PostMapping("/configuration/post")
    public String configurationPost(DatabaseDTO databaseDTO) {
        databaseService.saveDatabase(databaseDTO.toEntity());
        return "redirect:/admin/configuration";
    }

    @GetMapping("/admin/configuration")
    public String list(Model model) {
        List<DatabaseDTO> databaseDTOList = databaseService.getDatabaseList();
        model.addAttribute("databaseList", databaseDTOList);
        return "admin/configuration";
    }

    @PostMapping("/admin/configurationList")
    public String configurationList() {
        return "redirect:/admin/configuration";
    }

    @DeleteMapping("/admin/configuration/{id}")
    public String deleteDatabase(@PathVariable Integer id) {
        databaseService.deleteDatabase(id);
        return "redirect:/admin/configuration";
    }
}
