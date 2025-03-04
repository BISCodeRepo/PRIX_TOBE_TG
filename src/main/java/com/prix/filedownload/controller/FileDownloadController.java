package com.prix.filedownload.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;


@RestController
public class FileDownloadController {
    // actg sample data 다운로드
    @GetMapping("/sampledata/{fileName}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("/templates/livesearch/sampledata/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // actg 결과 zip 파일 다운로드
    @GetMapping("/actg/download")
    @ResponseBody
    public ResponseEntity<Resource> resultFileDownload(@RequestParam("index") String index) {
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
}
