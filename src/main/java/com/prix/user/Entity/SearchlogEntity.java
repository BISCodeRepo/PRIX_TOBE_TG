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

    @Column(nullable = false)
    private Integer userID;

    @Column(updatable = false)
    private LocalDate date;

    @Column(length = 100)
    private String title;

    private Integer msdata;

    private Integer db;

    private String engine;

    private String result;

    @Builder
    public SearchlogEntity(int id, Integer userID, String title, Integer  msdata,
                           Integer db,String engine, String result){
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.msdata = msdata;
        this.db = db;
        this.engine = engine;
        this.result = result;
    }
}
