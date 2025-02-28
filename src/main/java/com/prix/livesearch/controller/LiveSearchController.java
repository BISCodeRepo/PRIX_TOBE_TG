package com.prix.livesearch.controller;

import com.prix.livesearch.DTO.ActgProcessDTO;
import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.livesearch.service.ActgAdminService;
import com.prix.livesearch.service.ActgProcService;
import com.prix.livesearch.service.ActgResultService;
import com.prix.user.DTO.SearchlogDTO;
import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.UserRepository;
import com.prix.user.service.SearchlogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
//@RequestMapping("/livesearch")
@RequiredArgsConstructor
@Slf4j
public class LiveSearchController {

    private final ActgProcService actgProcService;
    private final ActgResultService actgResultService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final ActgAdminService actgAdminService;
    private final SearchlogService searchlogService;

    @GetMapping("/actg/result")
    public String showResultPage(Principal principal, Model model, HttpServletRequest request, HttpSession session) {
        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByUserID(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        ActgResultDTO actgResultDTO = actgResultService.processResult(id, principal, request, session);
        String fileIndex = actgResultDTO.getIndex();
        /* String resultFileDownloadPath = "C:/ACTG_db/ACTG_db/log/" + fileIndex + ".zip"; */
        String resultFileDownloadPath = "/actg/download?index=" + fileIndex;

        model.addAttribute("resultFileDownloadPath", resultFileDownloadPath);
        model.addAttribute("actgResultDTO", actgResultDTO);
        return "livesearch/ACTG/actgresult";
    }

    // admin search log에서 index 눌렀을 때
    @GetMapping("/actg/adminResult")
    public String adminResultPage(Principal principal, Model model, HttpServletRequest request, HttpSession session, @RequestParam("index") String index) {
        Integer id = searchlogService.getUserIDByResult(index);
        LocalDate date = searchlogService.getDateByResult(index);
        String title = searchlogService.getTitleByResult(index);

        ActgResultDTO actgResultDTO = actgAdminService.processResultAdmin(id, principal, request, session);
        actgResultDTO.setDate(date);
        actgResultDTO.setTitle(title);

        /* String resultFileDownloadPath = "C:/ACTG_db/ACTG_db/log/" + fileIndex + ".zip"; */
        String resultFileDownloadPath = "/actg/download?index=" + index;

        model.addAttribute("resultFileDownloadPath", resultFileDownloadPath);
        model.addAttribute("actgResultDTO", actgResultDTO);
        return "livesearch/ACTG/actgresult";
    }

    @GetMapping("/actg/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("index") String index) {
        String filePath = "C:/ACTG_db/ACTG_db/log/" + index + ".zip";
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // actg.html에서 submit 버튼을 누른 경우
    @PostMapping("/actg/actgprocess")
    public String processACTG(Principal principal, Model model, HttpServletRequest request, @RequestParam Map<String, String> paramsMap,
                                      @RequestParam("peptideFile") MultipartFile peptideFile,
                              @RequestParam(value = "mutationFile", required = false) MultipartFile mutationFile) {

        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByUserID(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        ActgProcessDTO actgProcessDTO = ActgProcessDTO.builder().rate("0%").failed(false).finished(false)
                .output("").title("").processName("").build();

        MultipartFile[] multipartFiles = { peptideFile, mutationFile };

        try {
            actgProcessDTO = actgProcService.process(id, request, paramsMap, multipartFiles);
        } catch (Exception e) {
            logger.error("ACTG processing error: {}", e.getMessage());
            e.printStackTrace();
        }


        // 정상적으로 종료된 경우 actgresult.html로 이동
        if (actgProcessDTO.isFinished()) {
            return "redirect:/actg/result?index=" + actgProcessDTO.getPrixIndex();
        }

        // processDTO.isFinished()가 false를 반환하는 경우 (처리 과정이 완료되지 않았을 경우)
        model.addAttribute("actgProcessDTO", actgProcessDTO);
        return "livesearch/ACTG/actgprocess";
    }

    // livesearch/ACTG/actgprocess에서 window.location 실행한 경우
    @GetMapping("/actg/actgprocess")
    public String getACTG(Principal principal, Model model, HttpServletRequest request, @RequestParam Map<String, String> paramsMap) {

        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByUserID(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        // dummy process dto
        ActgProcessDTO actgProcessDTO = ActgProcessDTO.builder()
                .rate("0%")
                .failed(false)
                .finished(false)
                .output("")
                .title("")
                .processName("")
                .build();

        try {
            // GET 요청에서는 파일을 업로드할 수 없기 때문에 파일을 제외하고 처리
            actgProcessDTO = actgProcService.process(id, request, paramsMap, null);
        } catch (Exception e) {
            logger.error("process service error: {}", e.getMessage());
            e.printStackTrace();
        }
        if (actgProcessDTO.isFinished()) {// 정상종료시 result페이지로 이동
            return "redirect:/actg/result?index=" + actgProcessDTO.getPrixIndex();
        }

        model.addAttribute("actgProcessDTO", actgProcessDTO);

        return "livesearch/ACTG/actgprocess";
    }

    @GetMapping("/username")
    public String username(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        log.info("사용자 이름 조회: {}", username);
        return username;
    }

    @GetMapping("/livesearch/use")
    public String use() {
        return "livesearch/use";
    }

    @GetMapping("/livesearch")
    public String livesearch() {
        return "livesearch/livesearchmain";
    }

    // 로그인 한 경우 ACTG 테이블에서 UserName에 userID 나오도록 함
    @GetMapping("/livesearch/ACTG/actg")
    public String actgUserID(Principal principal, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userID = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userID", userID);
        return "livesearch/ACTG/actg";
    }
}
