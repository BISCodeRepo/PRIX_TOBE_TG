package com.prix.user.Repository;

import com.prix.user.Entity.EnzymesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnzymeRepository extends JpaRepository<EnzymesEntity, Integer> {
}
