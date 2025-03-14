package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, name = "user_id")
    private Integer userID;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 8)
    private String ct_cleave;

    @Column(length = 8)
    private String nt_cleave;

    @Builder
    public EnzymesEntity(int id, int userID, String name, String ct_cleave, String nt_cleave){
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.ct_cleave = ct_cleave;
        this.nt_cleave = nt_cleave;
    }
}
