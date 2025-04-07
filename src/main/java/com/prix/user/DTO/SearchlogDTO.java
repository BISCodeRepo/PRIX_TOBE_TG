package com.prix.user.DTO;


import com.prix.user.Entity.SearchlogEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
public class SearchlogDTO {

    private Integer userId;

    private LocalDate date;

    private String title;

    private Integer msfile;

    private Integer db;

    private String engine;

    private Integer result;

    private String actg;

    public SearchlogEntity toEntity(){
        SearchlogEntity build = SearchlogEntity.builder()
                .userId(userId)
                .title(title)
                .msfile(msfile)
                .db(db)
                .engine(engine)
                .result(result)
                .actg(actg)
                .build();
        return build;
    }

    @Builder
    public SearchlogDTO(Integer userId, LocalDate date, String title,
                        Integer msfile, Integer db, String engine, Integer result, String actg){
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.msfile = msfile;
        this.db = db;
        this.engine = engine;
        this.result = result;
        this.actg = actg;
    }
}
