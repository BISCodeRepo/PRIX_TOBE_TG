package com.prix.user.service;

import com.prix.user.DTO.DatabaseDTO;
import com.prix.user.Entity.DatabaseEntity;

import java.util.List;


public interface DatabaseService {
    int saveDatabase(DatabaseEntity databaseEntity);

    List<DatabaseDTO> getDatabaseList();

    void deleteDatabase(int id);
}
