package com.prix.user.Repository;

import com.prix.user.Entity.ModificationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModificationLogRepository extends JpaRepository<ModificationLogEntity, Integer> {
}
