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

    private Integer userID;

    private LocalDate date;

    private String title;

    private Integer msdata;

    private Integer db;

    private String engine;

    private String result;

    public SearchlogEntity toEntity(){
        SearchlogEntity build = SearchlogEntity.builder()
                .userID(userID)
                .title(title)
                .msdata(msdata)
                .db(db)
                .engine(engine)
                .result(result)
                .build();
        return build;
    }

    @Builder
    public SearchlogDTO(Integer userID, LocalDate date, String title,
                        Integer msdata, Integer db, String engine, String result){
        this.userID = userID;
        this.date = date;
        this.title = title;
        this.msdata = msdata;
        this.db = db;
        this.engine = engine;
        this.result = result;
    }
}
