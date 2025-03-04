package com.prix.user.service;

import com.prix.user.DTO.UserDTO;
import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.UserRepository;
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
        if (userDTO.getName() == null || userDTO.getName().isEmpty() || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("UserName and password must not be empty");
        }
        // 동일한 사용자 이름을 가진 사용자가 이미 있는지 확인
        Optional<UserEntity> existingUser = userRepository.findByName(userDTO.getName());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("UserName already exists");
        }

        // 사용자 정보 저장
        UserEntity user = UserEntity.builder()
                .name(userDTO.getName())
                .email(userDTO.getName())
                .level(1)
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // 사용자 정보 저장 여부 확인 로그
        UserEntity savedUser = userRepository.save(user);
        logger.info("User registered: {}", savedUser);

        return userRepository.save(user);
    }

    @Override
    public boolean withdrawl (String name) {
        Optional<UserEntity> userEntity = userRepository.findByName(name);
        userEntity.ifPresent(userRepository::delete);
        return true;
    }

    @Override
    public boolean checkAdminLogin(String email, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
//            if (passwordEncoder.matches(password, user.getPassword()) && user.getLevel().equals("ADMIN")) {
            if (passwordEncoder.matches(password, user.getPassword()) && user.getLevel() == 2) {
                return true;
            }
        }
        return false;
    }
}