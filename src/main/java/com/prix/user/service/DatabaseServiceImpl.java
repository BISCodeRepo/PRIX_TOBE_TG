package com.prix.user.service;

import com.prix.user.DTO.DatabaseDTO;
import com.prix.user.Entity.DatabaseEntity;
import com.prix.user.Repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService{
    private final DatabaseRepository databaseRepository;

    public int saveDatabase(DatabaseEntity databaseEntity) {
        return databaseRepository.save(databaseEntity).getId();
    }

    @Override
    @Transactional
    public List<DatabaseDTO> getDatabaseList() {
        List<DatabaseEntity> databaseList = databaseRepository.findAll();
        List<DatabaseDTO> databaseDtoList = new ArrayList<>();

        for(DatabaseEntity databaseEntity : databaseList) {
            DatabaseDTO databaseDTO = DatabaseDTO.builder()
                    .id(databaseEntity.getId())
                    .data_id(databaseEntity.getData_id())
                    .file(null)
                    .fileName(databaseEntity.getFile())
                    .name(databaseEntity.getName())
                    .build();
            databaseDtoList.add(databaseDTO);
        }
        return databaseDtoList;
    }

    public void deleteDatabase(int id) {
        databaseRepository.deleteById(id);
    }

    @Override
    public void updateDatabase(int id, String name) {
        DatabaseEntity databaseEntity = databaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid database Id:" + id));
        databaseEntity.setName(name);
        databaseRepository.save(databaseEntity);
    }
}
