package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "px_software_log")
public class SoftwareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String date;

    @Column(length = 255, nullable = false)
    private String file;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 16, nullable = false)
    private String version;
}
