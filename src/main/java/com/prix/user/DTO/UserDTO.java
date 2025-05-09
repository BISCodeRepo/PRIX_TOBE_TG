package com.prix.user.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty(message = "Please input user name.")
    @Size(min = 2, max = 16, message = "User name must be between 2 and 16 characters.")
    private String name;

    @NotEmpty(message = "Please input password.")
    private String password;

    @NotEmpty(message = "Please input password again.")
    private String passwordConfirm;

    // 비밀번호와 비밀번호 확인이 일치하는지 확인
    public boolean isPasswordEqualToPasswordConfirm() {
        return password.equals(passwordConfirm);
    }
}
