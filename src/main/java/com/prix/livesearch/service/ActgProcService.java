package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgProcessDTO;
import com.prix.livesearch.mapper.ActgSearchlogRepository;
import com.prix.user.Repository.SearchlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private static final String PATH_ACTG_SEARCH = "/path/to/actg/search/";
    private static final String Path_Prix = "C:/PRIX_TOBE_TG/";

    public ActgProcessDTO process(Integer id, HttpServletRequest request, Map<String, String> paramsMap, MultipartFile[] files) throws IOException {
        ActgProcessDTO result = new ActgProcessDTO();

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

        if (request.getParameter("execute") == null) {
            Instant instant = Instant.now();
            long date = instant.toEpochMilli();
            String key = id + "_" + date;
            // TODO: processPath의 logDir 빼야 할 수도. 나중에 확인하기
            processPath = "process_" + key + ".proc";
            processName = processPath;
            String xmlPath = logDir + "param_" + key + ".xml";

            String peptideFilePath = "";
            String variantSpliceGraphDBPath = "";
            String proteinDBPath = "";
            String referenceGenomePath = "";
            String mutationFilePath = "";
            String outputPath = logDir;

            List<Object> combinedList = new ArrayList<>();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                combinedList.add(entry);
            }
            combinedList.addAll(Arrays.asList(files));

            for (Object obj : combinedList) {
                String name = "";
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
                } else if (obj instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) obj;
                    name = file.getName();

                    switch (name) {
                        case "peptideFile":
                            peptideFile = "peptide_" + key + ".txt";
                            peptideFilePath = logDir + peptideFile;

                            if (file.getSize() > 1024 * 100) {
                                failed = true;
                                output = "The size of peptide list should not exceed 1KB.";
                                break;
                            }

                            if (file.getOriginalFilename().length() > 0) {
                                try {
                                    FileOutputStream fos = new FileOutputStream(peptideFilePath);
                                    OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
                                    InputStream is = file.getInputStream();
                                    while (is.available() > 0)
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

                            if (file.getSize() > 20971520) {
                                failed = true;
                                output = "The size of VCF should not exceed 20MB.";
                                break;
                            }

                            if (file.getOriginalFilename().length() > 0) {
                                try {
                                    FileOutputStream fos = new FileOutputStream(mutationFilePath);
                                    OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
                                    InputStream is = file.getInputStream();
                                    while (is.available() > 0)
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

            if (!failed) {
                Runtime runtime = Runtime.getRuntime();
                String jarPath = Path_Prix + "src/main/resources/templates/livesearch/ACTG/ACTG_ver1.10.jar";

//                String[] command = {"cmd.exe", "/c", "java -Xss2M -Xmx8G -jar " + jarPath + " " + xmlPath + " " + logDir + processPath + " > " + logDir + "output.log 2> " + logDir + "error.log" };
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
            FileInputStream fis = new FileInputStream(processPath);
            StringWriter writer = new StringWriter();
            StringWriter allWriter = new StringWriter();

            while (fis.available() > 0) {
                char c = (char) fis.read();
                if (c == '\n') {
                    line = writer.toString();

                    if (line.indexOf("ERROR") >= 0 || line.indexOf("Exception") >= 0) {
                        failed = true;
                        logger.info("failed = true");
                    } else if (line.startsWith("Elapsed Time")) {
                        finished = true;
                    }

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
