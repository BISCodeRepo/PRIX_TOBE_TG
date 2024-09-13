package com.prix.user.DTO;


import com.prix.user.Entity.ModificationLogEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
public class ModificationLogDTO {
    private int id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String version;
    private MultipartFile file;

    public ModificationLogEntity toEntity(){
        ModificationLogEntity build = ModificationLogEntity.builder()
                .id(id)
                .date(date)
                .version(version)
                .file(file.getOriginalFilename())
                .build();
        return build;
    }

    @Builder
    public ModificationLogDTO(int id, Date  date, String version, MultipartFile file){
        this.id = id;
        this.date = date;
        this.version = version;
        this.file = file;
    }
}

