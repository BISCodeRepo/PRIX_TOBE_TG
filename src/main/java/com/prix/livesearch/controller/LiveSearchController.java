package com.prix.livesearch.controller;

import com.prix.livesearch.DTO.ActgProcessDTO;
import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.livesearch.service.ActgAdminService;
import com.prix.livesearch.service.ActgProcService;
import com.prix.livesearch.service.ActgResultService;
import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.UserRepository;
import com.prix.user.service.SearchlogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class LiveSearchController {

    private final ActgProcService actgProcService;
    private final ActgResultService actgResultService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final ActgAdminService actgAdminService;
    private final SearchlogService searchlogService;

    // actg process 후 result 페이지 이동 시
    @GetMapping("/actg/result")
    public String showResultPage(Principal principal, Model model, HttpServletRequest request) {
        // 로그인 x -> id = 4(anonymous)
        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByName(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        ActgResultDTO actgResultDTO = actgResultService.processResult(id, principal, request);
        String fileIndex = actgResultDTO.getIndex();
        String resultFileDownloadPath = "/actg/download?index=" + fileIndex;

        model.addAttribute("resultFileDownloadPath", resultFileDownloadPath);
        model.addAttribute("actgResultDTO", actgResultDTO);
        return "livesearch/ACTG/actgresult";
    }

    // admin search log에서 index 눌렀을 때
    @GetMapping("/actg/adminResult")
    public String adminResultPage(Principal principal, Model model, HttpServletRequest request, HttpSession session, @RequestParam("actg") String actg) {
        // id, date, title은 이미 searchlogEntity에 저장되어 있기 때문에 searchlogService에서 값을 가져오기
        Integer id = searchlogService.getUserIDByActg(actg);
        LocalDate date = searchlogService.getDateByActg(actg);
        String title = searchlogService.getTitleByActg(actg);

        // model에서 값을 읽어오지 않고 저장되어 있는 .proc 파일에서 값을 읽어와서 출력하기 위함
        ActgResultDTO actgResultDTO = actgAdminService.processResultAdmin(id, request);
        actgResultDTO.setDate(date);
        actgResultDTO.setTitle(title);

        String resultFileDownloadPath = "/actg/download?index=" + actg;

        model.addAttribute("resultFileDownloadPath", resultFileDownloadPath);
        model.addAttribute("actgResultDTO", actgResultDTO);
        return "livesearch/ACTG/actgresult";
    }

    // actg.html에서 submit 버튼을 누른 경우
    @PostMapping("/actg/actgprocess")
    public String processACTG(Principal principal, Model model, HttpServletRequest request, @RequestParam Map<String, String> paramsMap,
                                      @RequestParam("peptideFile") MultipartFile peptideFile,
                              @RequestParam(value = "mutationFile", required = false) MultipartFile mutationFile) {
        // 로그인 x -> id = 4(anonymous)
        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByName(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        // build로 actgProcessDTO 선언 후 초기화
        ActgProcessDTO actgProcessDTO = ActgProcessDTO.builder()
                .rate("0%")
                .failed(false)
                .finished(false)
                .output("")
                .title("")
                .processName("")
                .build();
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

    // livesearch/ACTG/actgprocess에서 window.location 실행한 경우(process 과정)
    @GetMapping("/actg/actgprocess")
    public String getACTG(Principal principal, Model model, HttpServletRequest request, @RequestParam Map<String, String> paramsMap) {

        // 로그인 x -> id = 4(anonymous)
        Integer id;
        if (principal != null) {
            Optional<UserEntity> userEntity = userRepository.findByName(principal.getName());
            id = (principal != null) ? userEntity.get().getId() : 4;
        } else {
            id = 4;
        }

        // build로 actgProcessDTO 선언 후 초기화
        ActgProcessDTO actgProcessDTO = ActgProcessDTO.builder()
                .rate("0%")
                .failed(false)
                .finished(false)
                .output("")
                .title("")
                .processName("")
                .build();

        try {
            actgProcessDTO = actgProcService.process(id, request, paramsMap, null);
        } catch (Exception e) {
            logger.error("process service error: {}", e.getMessage());
            e.printStackTrace();
        }
        // 정상적으로 종료된 경우 actgresult.html로 이동
        if (actgProcessDTO.isFinished()) {
            return "redirect:/actg/result?index=" + actgProcessDTO.getPrixIndex();
        }

        model.addAttribute("actgProcessDTO", actgProcessDTO);
        return "livesearch/ACTG/actgprocess";
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
    public String actgUserID(Principal principal, Model model) {
        String userID = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userID", userID);
        return "livesearch/ACTG/actg";
    }
}
