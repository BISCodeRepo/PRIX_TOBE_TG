package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "px_modification_log")
public class ModificationLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date date;

    @Column(length = 8)
    private String version;

    @Column(length = 128)
    private String file;

    @Builder
    public ModificationLogEntity(int id, Date date, String version, String file){
        this.id = id;
        this.date = date;
        this.version = version;
        this.file = file;
    }
}
