package com.prix.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserEntity register(UserDTO userDTO) {
        // 사용자 이름과 비밀번호의 유효성 검사
        if (userDTO.getUserID() == null || userDTO.getUserID().isEmpty() || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("UserID and password must not be empty");
        }
        // 동일한 사용자 이름을 가진 사용자가 이미 있는지 확인
        Optional<UserEntity> existingUser = userRepository.findByUserID(userDTO.getUserID());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("UserID already exists");
        }

        // 사용자 정보 저장
        UserEntity user = UserEntity.builder()
                .userID(userDTO.getUserID())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // 사용자 정보 저장 여부 확인 로그
        UserEntity savedUser = userRepository.save(user);
        logger.info("User registered: {}", savedUser);

        return userRepository.save(user);
    }

    @Override
    public boolean withdrawl (String userID) {
        Optional<UserEntity> userEntity = userRepository.findByUserID(userID);
        userEntity.ifPresent(userRepository::delete);
        return true;
    }

    @Override
    public boolean checkAdminLogin(String email, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            if (passwordEncoder.matches(password, user.getPassword()) && user.getRole().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}