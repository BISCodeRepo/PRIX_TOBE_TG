package com.prix.user.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "px_search_log")
public class SearchlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private Integer userId;

    @Column(updatable = false)
    private LocalDate date;

    @Column(length = 100)
    private String title;

    private Integer msfile;

    private Integer db;

    private String engine;

    private Integer result;

    private String actg;

    @Builder
    public SearchlogEntity(int id, Integer userId, String title, Integer msfile,
                           Integer db, String engine, Integer result, String actg){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.msfile = msfile;
        this.db = db;
        this.engine = engine;
        this.result = result;
        this.actg = actg;
    }
}
