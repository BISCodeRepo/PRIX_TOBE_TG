package com.prix.livesearch.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActgResultDTO {
    private String userName;
    private String title;
    private String proteinDB;
    private String proteinOption;
    private String VSGOption;
    private String variantSpliceGraphDB;
    private String method;
    private String IL;
    private String index;
    private LocalDate date;
}

