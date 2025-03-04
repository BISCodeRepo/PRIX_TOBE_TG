package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActgAdminService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH_ACTG_LOG = "C:/ACTG_db/ACTG_db/log/";
    private static final String PATH_ACTG_DB = "C:/ACTG_db/ACTG_db/";

    public ActgResultDTO processResultAdmin(Integer id, HttpServletRequest request) {
        // Environment
        String method = "";
        String IL = "";

        // Protein DB
        String proteinDB = null;
        String SAV = "";

        // Variant Splice Graph DB
        String variantSpliceGraphDB = null;
        String VSGOption = "";

        String index = request.getParameter("index");
        String key = id + "_" + index;

        String filePath = "process_" + key + ".proc";
        File file = new File(PATH_ACTG_LOG + filePath);
        logger.info("actgadminservice: {}", PATH_ACTG_LOG + filePath);

        // process_key.proc 파일이 저장되어 있는 경우 파일로부터 값 읽어오기
        if (file.exists()){
            try {
                FileReader FR = new FileReader(PATH_ACTG_LOG + filePath);
                BufferedReader BR = new BufferedReader(FR);
                String line = null;
                while ((line=BR.readLine()) != null) {
                    if (line.contains("Mapping method")) {
                        method = line.split(":")[1];
                    } else if (line.contains("Isoleucine is equivalent to leucine")) {
                        IL = "Isoleucine is equivalent to leucine";
                    } else if (line.contains("Protein database:")) {
                        String[] parts = line.split("/");
                        proteinDB = parts[parts.length - 1];
                    } else if (line.contains("Single amino-acids variantion")) {
                        SAV = "Single amino-acids variantion";
                    } else if (line.contains("Graph database:")) {
                        variantSpliceGraphDB = line.split("/")[-1];
                    } else if (line.contains("Junction variation")) {
                        if (VSGOption != "") {
                            VSGOption += ", junction variation";
                        } else {
                            VSGOption += "junction variation";
                        }
                    } else if (line.contains("Exon skipping")) {
                        if (VSGOption != "") {
                            VSGOption += ", exon skipping";
                        } else {
                            VSGOption += "exon skipping";
                        }
                    } else if (line.contains("Exon-extension")) {
                        if (VSGOption != "") {
                            VSGOption += ", intron mapping";
                        } else {
                            VSGOption += "intron mapping";
                        }
                    } else if (line.contains("VCF:")) {
                        if (VSGOption != "") {
                            VSGOption += ", mutation";
                        } else {
                            VSGOption += "mutation";
                        }
                    }
                }
                BR.close(); FR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        filePath = "param_" + key + ".xml";
        file = new File(PATH_ACTG_LOG + filePath);
        logger.info("actgadminservice: {}", PATH_ACTG_LOG + filePath);

        // param_key.xml 파일이 저장되어 있는 경우 파일로부터 값 읽어오기
        if (file.exists()){
            try {
                FileReader FR = new FileReader(PATH_ACTG_LOG + filePath);
                BufferedReader BR = new BufferedReader(FR);
                String line = null;
                while ((line=BR.readLine()) != null) {
                    if (line.contains("graphFile")) {
                        variantSpliceGraphDB = line.split("@")[1];
                        variantSpliceGraphDB = variantSpliceGraphDB.replace(PATH_ACTG_DB,"");
                    }
                }
                BR.close(); FR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ActgResultDTO.builder().userName(id.toString()).method(method).IL(IL).proteinDB(proteinDB)
                .proteinOption(SAV).VSGOption(VSGOption).variantSpliceGraphDB(variantSpliceGraphDB).index(index)
                .build();
    }
}
