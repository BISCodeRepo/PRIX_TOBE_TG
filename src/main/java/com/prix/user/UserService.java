package com.prix.user;

import com.prix.user.UserDTO;
import jakarta.transaction.Transactional;

public interface UserService {

    UserEntity register(UserDTO userDTO);

    boolean withdrawl(String userID);

    boolean checkAdminLogin(String email, String password);
}