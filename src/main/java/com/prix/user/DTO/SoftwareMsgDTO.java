package com.prix.user.DTO;

import com.prix.user.Entity.SoftwareMsgEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class SoftwareMsgDTO {
    private String id;
    private String message;

    public SoftwareMsgEntity toEntity(){
        SoftwareMsgEntity build = SoftwareMsgEntity.builder()
                .id(id)
                .message(message)
                .build();
        return build;
    }

    @Builder
    public SoftwareMsgDTO(String id, String message){
        this.id = id;
        this.message = message;
    }
}
