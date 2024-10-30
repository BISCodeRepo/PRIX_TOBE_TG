package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.user.Entity.SearchlogEntity;
import com.prix.user.Repository.SearchlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActgResultService {

    private final SearchlogRepository searchlogRepository;

    private static final String LOG_DIRECTORY_PATH = "/path/to/actg/log/";
    private static final String DATABASE_DIRECTORY_PATH = "/path/to/actg/db/";

    /**
     * 요청 및 사용자 ID를 기반으로 ACTG 검색 결과를 처리합니다.
     *
     * @param request 클라이언트 요청 정보
     * @param userId 사용자 ID
     * @return ActgResultDTO - 검색 결과 DTO
     */
    public ActgResultDTO processResult(HttpServletRequest request, Integer userId) {
        String index = request.getParameter("index");
        String xmlFileName = "param_" + userId + "_" + index + ".xml";

        Optional<SearchlogEntity> searchLogOpt = searchlogRepository.findByResult(index);
        if (searchLogOpt.isEmpty()) {
            log.warn("검색 로그가 없습니다. Index: {}", index);
            return null; // 결과가 없을 경우 null을 반환합니다.
        }

        SearchlogEntity searchLog = searchLogOpt.get();
        String userName = (searchLog.getUserID().equals("4")) ? "anonymous" : searchLog.getUserID().toString();
        LocalDate date = searchLog.getDate();
        String title = searchLog.getTitle();

        String method = null, IL = null, proteinDB = null, VSGOption = "";

        // XML 설정 파일을 읽고 필요한 정보를 추출합니다.
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_DIRECTORY_PATH + xmlFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("MappingMethod")) {
                    method = line.split("@")[1];
                } else if (line.contains("ILSame")) {
                    IL = line.split("@")[1].equalsIgnoreCase("yes") ? "Isoleucine is equivalent to leucine" : "";
                } else if (line.contains("graphFile")) {
                    proteinDB = line.split("@")[1].replace(DATABASE_DIRECTORY_PATH, "");
                }
            }
        } catch (IOException e) {
            log.error("XML 파일 읽기 오류", e);
            return null; // 파일 읽기 오류 시 null을 반환
        }

        // 결과 파일을 압축하여 반환합니다.
        createZipFile(LOG_DIRECTORY_PATH, index, generateResultFiles(userId, index));

        return ActgResultDTO.builder()
                .userName(userName)
                .title(title)
                .proteinDB(proteinDB)
                .VSGOption(VSGOption)
                .method(method)
                .IL(IL)
                .index(index)
                .date(date)
                .build();
    }

    // 파일 목록을 통해 압축 파일 생성
    private void createZipFile(String logDir, String index, String[] files) {
        String zipFilePath = logDir + index + ".zip";
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            byte[] buffer = new byte[1024];
            for (String fileName : files) {
                File file = new File(logDir + fileName);
                if (!file.exists()) continue;

                try (FileInputStream fis = new FileInputStream(file)) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, length);
                    }
                    zipOut.closeEntry();
                }
            }
        } catch (IOException e) {
            log.error("압축 파일 생성 오류", e);
        }
    }
    
    // 결과 파일 list 생성
    private String[] generateResultFiles(Integer userId, String index) {
        return new String[]{
                "SAV_peptide_" + userId + "_" + index + ".txt",
                "NOR_peptide_" + userId + "_" + index + ".txt",
                "VSG_peptide_" + userId + "_" + index + ".gff",
                "VSG_peptide_" + userId + "_" + index + ".flat",
                "VSG_peptide_" + userId + "_" + index + ".log",
                "SFT_peptide_" + userId + "_" + index + ".flat",
                "SFT_peptide_" + userId + "_" + index + ".gff"
        };
    }
}


