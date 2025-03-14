package com.prix.user.service;

import com.prix.user.DTO.SoftwareLogDTO;
import com.prix.user.Entity.SoftwareLogEntity;
import com.prix.user.Repository.SoftwareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareLogServiceImpl implements SoftwareLogService{
    private final SoftwareLogRepository softwareLogRepository;

    public int saveSoftwareLog(SoftwareLogEntity softwareLogEntity) {
        return softwareLogRepository.save(softwareLogEntity).getId();
    }

    @Override
    @Transactional
    public List<SoftwareLogDTO> getSoftwareLogList() {
        List<SoftwareLogEntity> softwareLogList = softwareLogRepository.findAll();
        List<SoftwareLogDTO> SoftwareLogDTOList = new ArrayList<>();

        for(SoftwareLogEntity softwareLogEntity : softwareLogList) {
            SoftwareLogDTO softwareLogDTO = SoftwareLogDTO.builder()
                    .id(softwareLogEntity.getId())
                    .name(softwareLogEntity.getName())
                    .date(softwareLogEntity.getDate())
                    .version(softwareLogEntity.getVersion())
                    .file(null)
                    .fileName(softwareLogEntity.getFile())
                    .build();
            SoftwareLogDTOList.add(softwareLogDTO);
        }
        return SoftwareLogDTOList;
    }
}
