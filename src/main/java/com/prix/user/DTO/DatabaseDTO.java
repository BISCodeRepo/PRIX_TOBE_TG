package com.prix.user.DTO;

import com.prix.user.Entity.DatabaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class DatabaseDTO {
    private int data_id;
    private String file;
    private String name;

    public DatabaseEntity toEntity(){
        DatabaseEntity build = DatabaseEntity.builder()
                .data_id(data_id)
                .file(file)
                .name(name)
                .build();
        return build;
    }

    @Builder
    public DatabaseDTO(int data_id, String file, String name){
        this.data_id = data_id;
        this.file = file;
        this.name = name;
    }
}
