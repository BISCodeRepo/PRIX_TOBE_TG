package com.prix.user.DTO;


import com.prix.user.Entity.EnzymesEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class EnzymeDTO {
    private int id;
    private int userID;
    private String name;
    private String ct_cleave;
    private String nt_cleave;

    public EnzymesEntity toEntity(){
        EnzymesEntity build = EnzymesEntity.builder()
                .id(id)
                .userID(userID)
                .name(name)
                .ct_cleave(ct_cleave)
                .nt_cleave(nt_cleave)
                .build();
        return build;
    }

    @Builder
    public EnzymeDTO(int id, int userID, String name, String ct_cleave, String nt_cleave){
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.ct_cleave = ct_cleave;
        this.nt_cleave = nt_cleave;
    }
}
