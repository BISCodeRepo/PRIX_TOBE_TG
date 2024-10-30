package com.prix.livesearch.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ActgResultDTO {
    private String userName;
    private String title;
    private String proteinDB;
    private String VSGOption;
    private String method;
    private String IL;
    private String index;
    private LocalDate date;
}

