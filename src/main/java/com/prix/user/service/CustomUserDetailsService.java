package com.prix.user.service;

import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.UserRepository;
import com.prix.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ObjectProvider<PasswordEncoder> passwordEncoderProvider;

    @Override
    public UserDetails loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
        System.out.println("로그인 시도 email: " + nameOrEmail);

        Optional<UserEntity> optionalUser = userRepository.findByEmail(nameOrEmail);

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByName(nameOrEmail);
        }

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        UserEntity user = optionalUser.get();
        System.out.println("DB 비밀번호 해시: " + user.getPassword());
        PasswordEncoder passwordEncoder = passwordEncoderProvider.getIfAvailable();
        System.out.println("입력 비밀번호 prixdev123과 매치: " +
                passwordEncoder.matches("prixdev123", user.getPassword()));

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getLevel() == 2) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(user.getEmail(), user.getPassword(), authorities); // 로그인 시 email 기준
    }
}

