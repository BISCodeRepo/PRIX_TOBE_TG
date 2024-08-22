package com.prix.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserID(String userID);

    Optional<UserEntity> findByEmail(String email);

}