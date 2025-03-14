package com.prix.user.Repository;

import com.prix.user.Entity.DatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Integer> {
}
