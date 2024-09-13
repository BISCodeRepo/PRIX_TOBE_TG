package com.prix.user.Repository;

import com.prix.user.Entity.SoftwareLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareLogRepository extends JpaRepository<SoftwareLogEntity, Integer> {
}
