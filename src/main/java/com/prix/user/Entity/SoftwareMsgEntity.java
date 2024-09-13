package com.prix.user.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "px_software_msg")
public class SoftwareMsgEntity {
    @Id
    private String id;
    private String message;

    @Builder
    public SoftwareMsgEntity(String id, String message){
        this.id = id;
        this.message = message;
    }
}
