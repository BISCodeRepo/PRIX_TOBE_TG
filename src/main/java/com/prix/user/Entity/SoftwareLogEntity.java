package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "px_software_log")
public class SoftwareLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date date;

    @Column(length = 255, nullable = false)
    private String file;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 16, nullable = false)
    private String version;

    @Builder
    public SoftwareLogEntity(int id, String name, Date date, String version, String file){
        this.id = id;
        this.name = name;
        this.date = date;
        this.version = version;
        this.file = file;
    }
}
