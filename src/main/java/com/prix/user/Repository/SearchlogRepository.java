package com.prix.user.Repository;

import com.prix.user.Entity.SearchlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchlogRepository extends JpaRepository<SearchlogEntity, Long> {
    List<SearchlogEntity> findByUserID(String userID);

    Optional<SearchlogEntity> findByResult(String index);
}
