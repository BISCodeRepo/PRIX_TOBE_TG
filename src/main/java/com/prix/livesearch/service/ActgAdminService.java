package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActgAdminService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH_ACTG_LOG = "C:/ACTG_db/ACTG_db/log/";
    private static final String PATH_ACTG_DB = "C:/ACTG_db/ACTG_db/";

    public ActgResultDTO processResultAdmin(Integer id, Principal principal, HttpServletRequest request, HttpSession session) {
        // Environment
        String method = "";
        String IL = "";

        // Protein DB
        String proteinDB = null;

        // Variant Splice Graph DB
        String variantSpliceGraphDB = null;

        String index = request.getParameter("index");
        String key = id + "_" + index;

        String filePath = "process_" + key + ".proc";
        File file = new File(PATH_ACTG_LOG + filePath);
        logger.info("actgadminservice: {}", PATH_ACTG_LOG + filePath);

        if (file.exists()){
            try {
                FileReader FR = new FileReader(PATH_ACTG_LOG + filePath);
                BufferedReader BR = new BufferedReader(FR);
                String line = null;
                while ((line=BR.readLine()) != null) {
                    if (line.contains("Mapping method")) {
                        method = line.split(":")[1];
                    } else if (line.contains("Protein database:")) {
                        String[] parts = line.split("/");
                        proteinDB = parts[parts.length - 1];
                    } else if (line.contains("Graph database:")) {
                        variantSpliceGraphDB = line.split("/")[-1];
                    }
                }
                BR.close(); FR.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ActgResultDTO.builder().userName(id.toString()).method(method).IL(IL).proteinDB(proteinDB)
                .proteinOption("").VSGOption("").variantSpliceGraphDB(variantSpliceGraphDB).index(index)
                .build();
    }
}
