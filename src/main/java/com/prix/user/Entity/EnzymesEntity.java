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
@Table(name = "px_enzyme")
public class EnzymesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userID;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 8)
    private String ct_cleave;

    @Column(length = 8)
    private String nt_cleave;
}
