package com.prix.user.Repository;

import com.prix.user.Entity.SearchlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchlogRepository extends JpaRepository<SearchlogEntity, Long> {
    List<SearchlogEntity> findByUserID(String userID);
}
