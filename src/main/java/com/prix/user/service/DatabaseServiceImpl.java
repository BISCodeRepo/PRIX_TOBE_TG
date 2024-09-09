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

    //TODO: 파일 업로드 시스템 만들기
    @Override
    @Transactional
    public List<DatabaseDTO> getDatabaseList() {
        List<DatabaseEntity> databaseList = databaseRepository.findAll();
        List<DatabaseDTO> databaseDtoList = new ArrayList<>();

        for(DatabaseEntity databaseEntity : databaseList) {
            DatabaseDTO databaseDTO = DatabaseDTO.builder()
                    .id(databaseEntity.getId())
                    .data_id(databaseEntity.getData_id())
                    //.file(databaseEntity.getFile())
                    .name(databaseEntity.getName())
                    .build();
            databaseDtoList.add(databaseDTO);
        }
        return databaseDtoList;
    }

    public void deleteDatabase(int id) {
        databaseRepository.deleteById(id);
    }
}
