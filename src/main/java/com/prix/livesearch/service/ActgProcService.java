package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgProcessDTO;
import com.prix.user.Repository.SearchlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ActgProcService {

    private final SearchlogRepository searchlogRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH_ACTG_LOG = "C:/ACTG_db/ACTG_db/log/";
    private static final String PATH_ACTG_DB = "C:/ACTG_db/ACTG_db/";
    private static final String Path_Prix = "C:/PRIX_TOBE_TG/";

    public ActgProcessDTO process(Integer id, HttpServletRequest request, Map<String, String> paramsMap, MultipartFile[] files) throws IOException {

        String user = "";
        String title = request.getParameter("title");

        // Environment
        String method = "";
        String peptideFile = "";
        String IL = "";

        // Protein DB
        String proteinDB = "";
        String SAV = "";

        // Variant Splice Graph DB
        String variantSpliceGraphDB = "";
        String mutation = "";
        String mutationFile = "";
        String exonSkipping = "";
        String altAD = "";
        String intron = "";

        // Six-frame translation
        String referenceGenome = "";

        String processName = request.getParameter("process");
        logger.info("process: {}", processName);

        String line = "";
        String rate = "0%";
        boolean finished = false;
        boolean failed = false;
        String output = "";

        final String logDir = PATH_ACTG_LOG;
        final String dbDir = PATH_ACTG_DB;
        log.info("logDir = {}", logDir);
        log.info("dbDir = {}", dbDir);

        String processPath = logDir + processName;

        // ActgProcService가 처음으로 실행되어 execute의 값이 null인 경우
        if (request.getParameter("execute") == null) {
            Instant instant = Instant.now();
            long date = instant.toEpochMilli();
            String key = id + "_" + date;
            processPath = "process_" + key + ".proc";
            processName = processPath;
            String xmlPath = logDir + "param_" + key + ".xml";

            String peptideFilePath = "";
            String variantSpliceGraphDBPath = "";
            String proteinDBPath = "";
            String referenceGenomePath = "";
            String mutationFilePath = "";
            String outputPath = logDir;

            // paramsMap과 files를 하나의 리스트로 결합
            List<Object> combinedList = new ArrayList<>();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                combinedList.add(entry);
            }
            combinedList.addAll(Arrays.asList(files));

            for (Object obj : combinedList) {
                String name = "";
                // combinedList에서 obj가 paramsMap인 경우
                if (obj instanceof Map.Entry<?, ?>) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    name = (String) entry.getKey();
                    String value = (String) entry.getValue();

                    switch (name) {
                        case "user":
                            user = value;
                            break;
                        case "title":
                            title = value;
                            break;
                        case "method":
                            method = value;
                            break;
                        case "IL":
                            IL = value;
                            break;
                        case "proteinDB":
                            proteinDB = value;
                            proteinDBPath = dbDir + proteinDB;
                            break;
                        case "SAV":
                            SAV = value;
                            break;
                        case "variantSpliceGraphDB":
                            variantSpliceGraphDB = value;
                            variantSpliceGraphDBPath = dbDir + variantSpliceGraphDB;
                            break;
                        case "mutation":
                            mutation = value;
                            break;
                        case "exonSkipping":
                            exonSkipping = value;
                            break;
                        case "altAD":
                            altAD = value;
                            break;
                        case "intron":
                            intron = value;
                            break;
                        case "referenceGenome":
                            referenceGenome = value;
                            referenceGenomePath = dbDir + referenceGenome;
                            break;
                        default:
                            break;
                    }
                } // combinedList에서 obj가 files인 경우
                else if (obj instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) obj;
                    name = file.getName();

                    switch (name) {
                        case "peptideFile":
                            peptideFile = "peptide_" + key + ".txt";
                            peptideFilePath = logDir + peptideFile;

                            // peptideFile 크기 제한
                            if (file.getSize() > 1024 * 100) {
                                failed = true;
                                output = "The size of peptide list should not exceed 1KB.";
                                break;
                            }

                            if (file.getOriginalFilename().length() > 0) { // 파일 이름이 존재한다면
                                try {
                                    FileOutputStream fos = new FileOutputStream(peptideFilePath);
                                    OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
                                    InputStream is = file.getInputStream();
                                    // 현재 읽을 수 있는 버퍼가 존재한다면
                                    while (is.available() > 0)
                                        // is를 통해서 읽은 문자를 writer의 write 함수를 통해 출력
                                        writer.write(is.read());
                                    writer.close();
                                    fos.close();
                                    is.close();
                                } catch (Exception e) {
                                    logger.warn("Error writing to peptide file: {}", e.getMessage());
                                }
                            }
                            break;
                        case "mutationFile":
                            mutationFile = "mutation_" + key + ".txt";
                            mutationFilePath = logDir + mutationFile;

                            // mutationFile 크기 제한
                            if (file.getSize() > 20971520) {
                                failed = true;
                                output = "The size of VCF should not exceed 20MB.";
                                break;
                            }

                            if (file.getOriginalFilename().length() > 0) { // 파일 이름이 존재한다면
                                try {
                                    FileOutputStream fos = new FileOutputStream(mutationFilePath);
                                    OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
                                    InputStream is = file.getInputStream();
                                    // 현재 읽을 수 있는 버퍼가 존재한다면
                                    while (is.available() > 0)
                                        // is를 통해서 읽은 문자를 writer의 write 함수를 통해 출력
                                        writer.write(is.read());
                                    writer.close();
                                    fos.close();
                                    is.close();
                                } catch (Exception e) {
                                    logger.warn("Error writing to mutation file: {}", e.getMessage());
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                // xml 파일 작성
                try {
                    FileReader FR = new FileReader(dbDir + "template.xml");
                    BufferedReader BR = new BufferedReader(FR);

                    FileWriter FW = new FileWriter(xmlPath);
                    BufferedWriter BW = new BufferedWriter(FW);

                    String line_ = null;

                    while ((line_ = BR.readLine()) != null) {
                        line_ = line_.replace("[METHOD]", method)
                                .replace("[IL]", IL)
                                .replace("[PEPTIDE_FILE]", peptideFilePath)
                                .replace("[PROTEIN_DB]", proteinDBPath)
                                .replace("[SAV]", SAV)
                                .replace("[VARIANT_SPLICE_GRAPH_DB]", variantSpliceGraphDBPath)
                                .replace("[ALT_AD]", altAD)
                                .replace("[EXON_SKIPPING]", exonSkipping)
                                .replace("[INTRON]", intron)
                                .replace("[MUTATION]", mutation)
                                .replace("[MUTATION_FILE]", mutationFilePath)
                                .replace("[REFERENCE_GENOME]", referenceGenomePath)
                                .replace("[OUTPUT]", outputPath);
                        BW.append(line_);
                        BW.newLine();
                    }
                    BR.close();
                    FR.close();
                    BW.close();
                    FW.close();

                } catch (IOException e) {
                    output = e + "\n";
                }
            }

            // 실패하지 않은 경우 ACTG_Search.jar 파일 실행
            if (!failed) {
                Runtime runtime = Runtime.getRuntime();
                String jarPath = Path_Prix + "src/main/resources/templates/livesearch/ACTG/ACTG_ver1.10.jar";

                // jar 파일은 logDir에 있는 xml 파일과 프로세스 파일을 인수로 받아 작업 수행
                // -Xss2M로 각 스레드 스택 크기 2mb로 설정, -Xmx8G로 힙 메모리 최대 크기 8GB로 설정
                String[] command = {"cmd.exe", "/c", "java -Xss2M -Xmx8G -jar " + jarPath + " " + xmlPath + " " + logDir + processPath};
                logger.info("Executing command: {}", String.join(" ", command));

                try {
                    Process process = runtime.exec(command);

                    // 프로세스의 출력 로그를 읽어 파일에 기록
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    FileWriter logWriter = new FileWriter(logDir + "ACTG_Search.log", true);
                    String s;
                    while ((s = stdInput.readLine()) != null) {
                        logWriter.write("OUTPUT: " + s + "\n");
                    }
                    while ((s = stdError.readLine()) != null) {
                        logWriter.write("ERROR: " + s + "\n");
                    }
                    logWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    failed = true;
                }
            }
        } else {
            // 'execute' 매개변수가 있는 경우 프로세스 파일 읽기
            FileInputStream fis = new FileInputStream(processPath);
            StringWriter writer = new StringWriter();
            StringWriter allWriter = new StringWriter();

            while (fis.available() > 0) {
                char c = (char) fis.read();
                if (c == '\n') {
                    line = writer.toString();

                    // 프로세스 출력에서 오류 또는 완료 확인
                    if (line.indexOf("ERROR") >= 0 || line.indexOf("Exception") >= 0) {
                        failed = true;
                        logger.info("failed = true");
                    } else if (line.startsWith("Elapsed Time")) {
                        finished = true;
                    }

                    // 출력에서 디렉토리 경로 대체
                    if (line.contains(logDir)) {
                        line = line.replace(logDir, "");
                    }
                    if (line.contains(dbDir)) {
                        line = line.replace(dbDir, "");
                    }

                    if (line.contains("%")) {
                        rate = line;
                    } else {
                        allWriter.append(line + "\n");
                    }
                    writer = new StringWriter();
                } else {
                    writer.append(c);
                }
            }

            if (writer.toString().length() > 0) {
                line = writer.toString();
            }

            fis.close();
            output = allWriter.toString();

            // 프로세스가 완료된 경우 검색 로그 업데이트 및 결과 반환
            if (finished) {
                String prixIndex = processPath.replace("process_" + id + "_", "");
                prixIndex = prixIndex.replace(logDir, "");
                prixIndex = prixIndex.replace(".proc", "");

                searchlogRepository.insert(
                        id, title.replace("'", "\\'"), 0, 0, prixIndex, "ACTG");

                ActgProcessDTO processDTO = ActgProcessDTO.builder().failed(failed).finished(finished).output(output)
                        .processName(processName).prixIndex(prixIndex).rate((rate)).title(title).build();
                logger.warn("processName2in:~~~~~~~~~~~{}", processName);
                return processDTO;
            }

        }
        // if 문 내부의 조건이 충족되지 않았을 때도 ACTGProcessDTO 객체가 반환되도록 보장하기 위해
        ActgProcessDTO processDTO = ActgProcessDTO.builder().failed(failed).finished(finished).output(output)
                .processName(processName).prixIndex("").rate((rate)).title(title).build();

        logger.warn("processName2out:~~~~~~~~~~~{}", processName);
        return processDTO;
    }
}
