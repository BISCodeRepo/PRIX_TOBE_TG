package com.prix.user.Repository;

import com.prix.user.Entity.SearchlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchlogRepository extends JpaRepository<SearchlogEntity, Long> {
}
