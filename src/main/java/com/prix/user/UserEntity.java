package com.prix.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "px_account")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 사용자 식별자

    //값 중복 불가
    @Column(unique = true)
    private String userID;

    private String password;

    //logger.info("Saved UserEntity: {}", savedUser);를 위해 추가
    public String toString() {
        return "UserEntity{id=" + this.getId() + ", userID=" + this.getUserID() + '\'' + "}";
    }

    //@Column(unique = true)
    //private String email;

    //private String role; // 사용자 권한 정보

}
