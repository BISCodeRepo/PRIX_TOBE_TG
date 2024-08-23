package com.prix.user;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchlogRepository extends JpaRepository<SearchlogEntity, Long> {
}
