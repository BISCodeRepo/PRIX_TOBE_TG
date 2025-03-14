package com.prix.user.service;


import com.prix.user.DTO.ModificationLogDTO;
import com.prix.user.Entity.ModificationLogEntity;

import java.util.List;

public interface ModificationLogService {
    int saveModificationLog(ModificationLogEntity modificationLogEntity);

    List<ModificationLogDTO> getModificationLogList();
}
