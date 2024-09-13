package com.prix.user.service;

import com.prix.user.DTO.SoftwareLogDTO;
import com.prix.user.Entity.SoftwareLogEntity;

import java.util.List;

public interface SoftwareLogService {
    int saveSoftwareLog(SoftwareLogEntity softwareLogEntity);

    List<SoftwareLogDTO> getSoftwareLogList();
}
