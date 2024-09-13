package com.prix.user.DTO;

import com.prix.user.Entity.SoftwareLogEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
public class SoftwareLogDTO {
    private int id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String version;
    private MultipartFile file;

    public SoftwareLogEntity toEntity(){
        SoftwareLogEntity build = SoftwareLogEntity.builder()
                .id(id)
                .name(name)
                .date(date)
                .version(version)
                .file(file.getOriginalFilename())
                .build();
        return build;
    }

    @Builder
    public SoftwareLogDTO(int id, String name, Date  date, String version, MultipartFile file){
        this.id = id;
        this.name = name;
        this.date = date;
        this.version = version;
        this.file = file;
    }
}
