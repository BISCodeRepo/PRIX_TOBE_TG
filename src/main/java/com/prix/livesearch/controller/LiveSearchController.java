package com.prix.livesearch.controller;

import com.prix.livesearch.DTO.ActgDTO;
import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.livesearch.service.ActgProcService;
import com.prix.livesearch.service.ActgResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
//@RequestMapping("/livesearch")
@RequiredArgsConstructor
@Slf4j
public class LiveSearchController {

    private final ActgProcService actgProcService;
    private final ActgResultService actgResultService;

    @PostMapping("/actg/upload")
    public ActgDTO uploadAndProcessACTG(
            HttpServletRequest request,
            @RequestParam Map<String, String> params,
            @RequestParam("files") MultipartFile[] files) {

        ActgDTO result = new ActgDTO();
        try {
            result = actgProcService.process(params.get("userId"), request, params, files);
            if (result.isFailed()) {
                log.warn("ACTG 처리 실패: {}", result.getOutput());
            } else {
                log.info("ACTG 처리가 성공적으로 완료되었습니다.");
            }
        } catch (IOException e) {
            log.error("ACTG 처리 중 I/O 오류 발생: {}", e.getMessage());
            result.setFailed(true);
            result.setOutput("파일 처리 오류: " + e.getMessage());
        } catch (Exception e) {
            log.error("ACTG 처리 중 오류 발생: {}", e.getMessage());
            result.setFailed(true);
            result.setOutput("처리 오류: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/actg/result")
    public ActgResultDTO showResultPage(HttpServletRequest request, @RequestParam Integer userId) {
        try {
            ActgResultDTO result = actgResultService.processResult(request, userId);
            if (result == null) {
                log.warn("ACTG 결과를 찾을 수 없습니다. 사용자 ID: {}", userId);
            }
            return result;
        } catch (Exception e) {
            log.error("ACTG 결과 처리 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }

    // sample data 다운로드
    @GetMapping("/actg/download")
    public ResponseEntity<InputStreamResource> downloadResult(@RequestParam String fileName) {
        try {
            File file = new File("/path/to/actg/log/" + fileName);
            if (!file.exists()) {
                log.warn("파일이 존재하지 않습니다: {}", fileName);
                return ResponseEntity.notFound().build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);

        } catch (IOException e) {
            log.error("파일 다운로드 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/actg/process")
    public ActgDTO processACTG(HttpServletRequest request, @RequestParam Map<String, String> params, @RequestParam("files") MultipartFile[] files) {
        return uploadAndProcessACTG(request, params, files);
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
    public String actgUserID(Principal principal, Model model) {
        String userID = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userID", userID);
        return "livesearch/ACTG/actg";
    }
}
