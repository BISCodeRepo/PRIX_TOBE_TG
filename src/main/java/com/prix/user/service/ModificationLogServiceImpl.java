package com.prix.user.service;

import com.prix.user.DTO.ModificationLogDTO;
import com.prix.user.Entity.ModificationLogEntity;
import com.prix.user.Repository.ModificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModificationLogServiceImpl implements ModificationLogService{

    private final ModificationLogRepository modificationLogRepository;

    public int saveModificationLog(ModificationLogEntity modificationLogEntity) {
        return modificationLogRepository.save(modificationLogEntity).getId();
    }

    @Override
    @Transactional
    public List<ModificationLogDTO> getModificationLogList() {
        List<ModificationLogEntity> modificationLogList = modificationLogRepository.findAll();
        List<ModificationLogDTO> modificationLogDTOList = new ArrayList<>();

        for(ModificationLogEntity modificationLogEntity : modificationLogList) {
            ModificationLogDTO modificationLogDTO = ModificationLogDTO.builder()
                    .id(modificationLogEntity.getId())
                    .date(modificationLogEntity.getDate())
                    .version(modificationLogEntity.getVersion())
                    .file(null)
                    .fileName(modificationLogEntity.getFile())
                    .build();
            modificationLogDTOList.add(modificationLogDTO);
        }
        return modificationLogDTOList;
    }
}
