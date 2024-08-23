package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "px_database")
public class DatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer data_id;

    @Column(length = 128)
    private String file;

    @Column(length = 128)
    private String name;

    @Builder
    public DatabaseEntity(int id, int data_id, String file, String name){
        this.id = id;
        this.data_id = data_id;
        this.file = file;
        this.name = name;
    }
}
