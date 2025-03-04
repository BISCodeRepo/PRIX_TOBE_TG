package com.prix.livesearch.service;

import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.user.Entity.SearchlogEntity;
import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.SearchlogRepository;
import com.prix.user.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActgResultService {

    private final SearchlogRepository searchlogRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH_ACTG_LOG = "C:/ACTG_db/ACTG_db/log/";
    private static final String PATH_ACTG_DB = "C:/ACTG_db/ACTG_db/";
    private final UserRepository userRepository;

    public ActgResultDTO processResult(Integer id, Principal principal, HttpServletRequest request, HttpSession session) {

        String userName = null;
        String title = null;

        String index = request.getParameter("index");
        String key = id + "_" + index;
        String xmlPath = "param_" + key + ".xml";
        String SAVOutput = "SAV_peptide_" + key + ".txt";
        String NOROutput = "NOR_peptide_" + key + ".txt";
        String VSGGFFOutput = "VSG_peptide_" + key + ".gff";
        String VSGFlatOutput = "VSG_peptide_" + key + ".flat";
        String VSGLogOutput = "VSG_peptide_" + key + ".log";
        String SFTFlatOutput = "SFT_peptide_" + key + ".flat";
        String SFTGFFOutput = "SFT_peptide_" + key + ".gff";

        String[] files = new String[7];
        files[0] = SAVOutput;
        files[1] = NOROutput;
        files[2] = VSGGFFOutput;
        files[3] = VSGFlatOutput;
        files[4] = VSGLogOutput;
        files[5] = SFTFlatOutput;
        files[6] = SFTGFFOutput;

        //TODO: LocalDate인지 Date인지 결정
        LocalDate date = null;

        Optional<SearchlogEntity> searchlogEntity = Optional.empty();
        if(index != null){
            searchlogEntity = searchlogRepository.getSearchLog(index);
        }
        if (searchlogEntity.isPresent()) {
            title = searchlogEntity.get().getTitle();
            Integer userId = searchlogEntity.get().getUserId();
            if (userId == 4) {
                userName = "anonymous";
            }
            else {
//                Optional<UserEntity> userEntity = userRepository.findById(userId);
//                userName = userEntity.get().getUserID();
                userName = principal.getName();
            }
            date = searchlogEntity.get().getDate();
        }

        String method = null;
        String IL = null;
        String proteinDB = null;
        String SAV = null;
        String variantSpliceGraphDB = null;
        String altAD = null;
        String exonSkipping = null;
        String intron = null;
        String mutation = null;
        String referenceGenome = null;
        String proteinOption = "";
        String VSGOption = "";

        try{
            FileReader FR = new FileReader(PATH_ACTG_LOG + xmlPath);
            BufferedReader BR = new BufferedReader(FR);
            logger.info("path: {}", PATH_ACTG_LOG + xmlPath);
            String line = null;
            while ((line=BR.readLine()) != null) {
                if (line.contains("MappingMethod")) {
                    method = line.split("@")[1];
                    logger.info("method: {}", method);
                } else if (line.contains("ILSame")) {
                    IL = line.split("@")[1];
                    if (IL.equalsIgnoreCase("yes")) {
                        IL = "Isoleucine is equivalent to leucine";
                    } else {
                        IL = "";
                    }
                } else if(line.contains("graphFile")) {
                    variantSpliceGraphDB = line.split("@")[1];
                    variantSpliceGraphDB = variantSpliceGraphDB.replace(PATH_ACTG_DB,"");
                } else if (line.contains("type=\"proteinDB\"")) {
                    proteinDB = line.split("@")[1];
                    proteinDB = proteinDB.replace(PATH_ACTG_DB,"");
                } else if (line.contains("JunctionVariation")) {
                    altAD = line.split("@")[1];
                    if (altAD.equalsIgnoreCase("yes")) {
                        altAD = "junction variation";
                    } else {
                        altAD = "";
                    }

                    if (altAD.length() != 0) {

                        if (VSGOption.length() != 0) {
                            VSGOption = VSGOption + ", " + altAD;
                        } else {
                            VSGOption = altAD;
                        }
                    }
                } else if (line.contains("ExonSkipping")) {
                    exonSkipping = line.split("@")[1];
                    if (exonSkipping.equalsIgnoreCase("yes")) {
                        exonSkipping = "exon skipping";
                    } else {
                        exonSkipping = "";
                    }

                    if (exonSkipping.length() != 0) {

                        if (VSGOption.length() != 0) {
                            VSGOption = VSGOption + ", " + exonSkipping;
                        } else {
                            VSGOption = exonSkipping;
                        }
                    }
                } else if (line.contains("IntronMapping")) {
                    intron = line.split("@")[1];
                    if (intron.equalsIgnoreCase("yes")) {
                        intron = "intron mapping";
                    } else {
                        intron = "";
                    }

                    if (intron.length() != 0) {

                        if (VSGOption.length() != 0) {
                            VSGOption = VSGOption+", "+intron;
                        } else {
                            VSGOption = intron;
                        }
                    }
                } else if (line.contains("<Mutation>")) {
                    mutation = line.split("@")[1];
                    mutation = mutation.replace(PATH_ACTG_LOG,"");

                    if (mutation.length() != 0) {
                        mutation = "mutation";
                        if (VSGOption.length() != 0) {
                            VSGOption = VSGOption+", "+mutation;
                        } else {
                            VSGOption = mutation;
                        }
                    }
                } else if (line.contains("referenceGenome")) {
                    referenceGenome = line.split("@")[1];
                    referenceGenome = referenceGenome.replace(PATH_ACTG_DB,"");
                } else if (line.contains("SAV")) {
                    SAV = line.split("@")[1];
                    if (SAV.equalsIgnoreCase("yes")) {
                        SAV = "single amino-acid variation";
                    } else {
                        SAV = "";
                    }
                    proteinOption = SAV;
                }
            }
            BR.close(); FR.close();

        }catch(IOException e){
            logger.error("Error reading file: {}", PATH_ACTG_LOG + xmlPath, e);
        }

        try{
            byte[] buf = new byte[1024];
            String zip = PATH_ACTG_LOG + index + ".zip";

            File zipFile = new File(zip);
            if (!zipFile.exists()) {
                ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zip));
                for (int i = 0; i < files.length; i++) {
                    File file = new File(PATH_ACTG_LOG + files[i]);
                    if (!file.exists()) {
                        continue;
                    }
                    FileInputStream in = new FileInputStream(file);
                    outZip.putNextEntry(new ZipEntry(files[i]));

                    int len;
                    while ((len = in.read(buf)) > 0) {
                        outZip.write(buf, 0, len);
                    }
                    outZip.closeEntry();
                    in.close();
                }
                outZip.close();
            }
        }catch(Exception e){
            logger.error("Error occurred while creating ZIP file: {}", e.getMessage(), e);
        }

        return ActgResultDTO.builder().date(date).userName(userName).title(title).method(method).IL(IL).proteinDB(proteinDB)
                .proteinOption(proteinOption).VSGOption(VSGOption).variantSpliceGraphDB(variantSpliceGraphDB).index(index)
                .build();
    }
}


