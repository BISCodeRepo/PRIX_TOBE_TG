package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgDTO;
import com.prix.livesearch.mapper.ActgSearchlogRepository;
import com.prix.user.Repository.SearchlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActgProcService {

    private final SearchlogRepository searchlogRepository;
    private final ActgSearchlogRepository actgSearchlogRepository;

    private static final String PATH_ACTG_LOG = "/path/to/actg/log/";
    private static final String PATH_ACTG_DB = "/path/to/actg/db/";
    private static final String PATH_ACTG_SEARCH = "/path/to/actg/search/";

    public ActgDTO process(String id, HttpServletRequest request, Map<String, String> params, MultipartFile[] files) throws IOException {
        ActgDTO result = new ActgDTO();

        String user = "";
        String title = params.get("title");
        String method = "", peptideFile = "", IL = "";
        String proteinDB = "", SAV = "", variantSpliceGraphDB = "";
        String mutation = "", exonSkipping = "", altAD = "", intron = "", referenceGenome = "";
        String peptideFilePath = "", mutationFilePath = "", variantSpliceGraphDBPath = "", proteinDBPath = "", referenceGenomePath = "";

        boolean failed = false;
        String output = "";

        final String logDir = PATH_ACTG_LOG;
        final String dbDir = PATH_ACTG_DB;
        log.info("logDir = {}", logDir);
        log.info("dbDir = {}", dbDir);

        String processName = params.get("process");
        String processPath = logDir + processName;
        Date date = new Date();
        String key = id + "_" + date.getTime();
        processPath = logDir + "process_" + key + ".proc";
        processName = processPath;
        String xmlPath = logDir + "param_" + key + ".xml";

        // 파라미터 설정 및 파일 경로 지정
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();

            switch (name) {
                case "user": user = value; break;
                case "title": title = value; break;
                case "method": method = value; break;
                case "IL": IL = value; break;
                case "proteinDB": proteinDB = value; proteinDBPath = dbDir + proteinDB; break;
                case "SAV": SAV = value; break;
                case "variantSpliceGraphDB": variantSpliceGraphDB = value; variantSpliceGraphDBPath = dbDir + variantSpliceGraphDB; break;
                case "mutation": mutation = value; break;
                case "exonSkipping": exonSkipping = value; break;
                case "altAD": altAD = value; break;
                case "intron": intron = value; break;
                case "referenceGenome": referenceGenome = value; referenceGenomePath = dbDir + referenceGenome; break;
                default: break;
            }
        }

        // 파일 처리
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fieldName = file.getName();
            if (fieldName.equals("peptideFile")) {
                peptideFile = "peptide_" + key + ".txt";
                peptideFilePath = logDir + peptideFile;

                if (file.getSize() > 1024 * 100) { // 1KB 제한
                    result.setFailed(true);
                    result.setOutput("The size of peptide list should not exceed 1KB.");
                    return result;
                }
                saveFile(file, peptideFilePath, "UTF-8");

            } else if (fieldName.equals("mutationFile")) {
                mutationFilePath = "/home/PRIX/ACTG_log/" + "mutation_" + key + ".txt";

                if (file.getSize() > 20971520) { // 20MB 제한
                    result.setFailed(true);
                    result.setOutput("The size of VCF should not exceed 20MB.");
                    return result;
                }
                saveFile(file, mutationFilePath, "UTF-8");
            }
        }

        // XML 파일 생성 및 프로세스 실행
        createXmlFile(dbDir + "template.xml", xmlPath, method, IL, peptideFilePath, proteinDBPath, SAV,
                variantSpliceGraphDBPath, altAD, exonSkipping, intron, mutation, mutationFilePath, referenceGenomePath, logDir);

        // 외부 프로세스 실행
        try {
            executeExternalProcess(xmlPath, processPath);
        } catch (Exception e) {
            result.setFailed(true);
            result.setOutput("프로세스 실행 중 오류: " + e.getMessage());
            return result;
        }

        // 프로세스 실행 결과 처리
        result.setFailed(failed);
        result.setOutput(output);
        return result;
    }

    private void saveFile(MultipartFile file, String filePath, String encoding) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
             InputStream is = file.getInputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                writer.write(new String(buffer, 0, bytesRead, encoding));
            }
            log.info("파일 저장 완료: {}", filePath);
        }
    }

    private void createXmlFile(String templatePath, String xmlPath, String method, String IL, String peptideFilePath,
                               String proteinDBPath, String SAV, String variantSpliceGraphDBPath, String altAD,
                               String exonSkipping, String intron, String mutation, String mutationFilePath,
                               String referenceGenomePath, String outputPath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(templatePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(xmlPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("[METHOD]", method)
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
                bw.write(line);
                bw.newLine();
            }
        }
    }

    private void executeExternalProcess(String xmlPath, String processPath) throws IOException, InterruptedException {
        String jarPath = PATH_ACTG_SEARCH + "ACTG_Search.jar";
        String[] command = {"java", "-Xss2M", "-Xmx10G", "-jar", jarPath, xmlPath, processPath};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("프로세스가 오류 코드로 종료되었습니다: " + exitCode);
            }
        }
    }
}
