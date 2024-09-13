package com.prix.user.Repository;

import com.prix.user.Entity.SoftwareMsgEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public interface SoftwareMsgRepository extends JpaRepository<SoftwareMsgEntity, Integer> {
}
