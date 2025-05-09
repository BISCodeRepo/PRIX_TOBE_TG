package com.prix.user.controller;

import com.prix.user.DTO.*;
import com.prix.user.Entity.DatabaseEntity;
import com.prix.user.Entity.EnzymesEntity;
import com.prix.user.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConfigurationController {
    private final DatabaseService databaseService;
    private final EnzymeService enzymeService;
    private final ModificationLogService modificationLogService;
    private final SoftwareLogService softwareLogService;
    private final SoftwareMsgService softwareMsgService;

    // admin configuration에서 테이블 수정 시
    @PostMapping("/configuration/post")
    public String configurationPost(@RequestParam("table") String table,
                                    DatabaseDTO databaseDTO, EnzymeDTO enzymeDTO, ModificationLogDTO modificationLogDTO,
                                    SoftwareLogDTO softwareLogDTO, SoftwareMsgDTO softwareMsgDTO) {
        if ("database".equals(table)) {
            String fileName = databaseDTO.getFile().getOriginalFilename();
            databaseService.saveDatabase(databaseDTO.toEntity(fileName));
        } else if ("enzyme".equals(table)) {
            enzymeService.saveEnzyme(enzymeDTO.toEntity());
        } else if ("modification".equals(table)) {
            String fileName = modificationLogDTO.getFile().getOriginalFilename();
            modificationLogService.saveModificationLog(modificationLogDTO.toEntity(fileName));
        } else if ("software_log".equals(table)) {
            String fileName = softwareLogDTO.getFile().getOriginalFilename();
            softwareLogService.saveSoftwareLog(softwareLogDTO.toEntity(fileName));
        } else if ("software_msg".equals(table)) {
            softwareMsgService.saveSoftwareMsg(softwareMsgDTO.toEntity());
        }
        return "redirect:/admin/configuration";
    }

    // admin/configuration.html로 이동 시
    @GetMapping("/admin/configuration")
    public String list(Model model) {
        List<DatabaseDTO> databaseDTOList = databaseService.getDatabaseList();
        model.addAttribute("databaseList", databaseDTOList);

        List<EnzymeDTO> enzymeDTOList = enzymeService.getEnzymeList();
        model.addAttribute("enzymeList", enzymeDTOList);

        List<ModificationLogDTO> modificationLogDTOList = modificationLogService.getModificationLogList();
        model.addAttribute("modificationLogList", modificationLogDTOList);

        List<SoftwareLogDTO> softwareLogDTOList = softwareLogService.getSoftwareLogList();
        model.addAttribute("softwareLogList", softwareLogDTOList);

        model.addAttribute("modaMessage", softwareMsgService.returnModaMessage());
        model.addAttribute("dbondMessage", softwareMsgService.returnDbondMessage());
        model.addAttribute("searchMessage", softwareMsgService.returnSearchMessage());
        model.addAttribute("signMessage", softwareMsgService.returnSignMessage());

        return "admin/configuration";
    }

    //database table 값 수정/삭제
    @PostMapping("/admin/configuration/databaseControl")
    public String databaseControl(@ModelAttribute("Database") DatabaseEntity databaseEntity,
                                  @RequestParam("databaseControl") String databaseControl) {
        if ("edit".equals(databaseControl)) { // edit 버튼을 눌렀을 때 name이랑 id만 업데이트
            databaseService.updateDatabase(databaseEntity.getId(), databaseEntity.getName());
        } else if ("unlink".equals(databaseControl)) { // unlink 버튼을 눌렀을 때 행 삭제
            databaseService.deleteDatabase(databaseEntity.getId());
        }
        return "redirect:/admin/configuration";
    }

    //enzyme table 값 수정/삭제
    @PostMapping("/admin/configuration/enzymeControl")
    public String enzymeControl(@ModelAttribute("enzyme")EnzymesEntity enzymesEntity,
                                @RequestParam("enzymeControl") String enzymeControl) {
        if ("edit".equals(enzymeControl)) {
            enzymeService.updateEnzyme(enzymesEntity.getId(), enzymesEntity.getName(), enzymesEntity.getCt_cleave(), enzymesEntity.getNt_cleave());
        } else if ("delete".equals(enzymeControl)) {
            enzymeService.deleteEnzyme(enzymesEntity.getId());
        }
        return "redirect:/admin/configuration";
    }

    // softwareMsg table 값 수정
    @PostMapping("/admin/configuration/softwareMsgControl")
    public String softwareMsgControl(@RequestParam("modaMessage") String modaMessage,
                                     @RequestParam("dbondMessage") String dbondMessage,
                                     @RequestParam("searchMessage") String searchMessage,
                                     @RequestParam("signMessage") String signMessage) {

        softwareMsgService.updateSoftwareMsg(modaMessage, dbondMessage, searchMessage, signMessage);
        return "redirect:/admin/configuration";
    }

}
