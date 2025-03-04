package com.prix.livesearch.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActgProcessDTO {

    private String rate;
    private boolean failed;
    private boolean finished;
    private String output;
    private String processName;
    private String prixIndex;
    private String title;
}

