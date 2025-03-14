package com.prix.user.service;

import com.prix.user.DTO.SoftwareMsgDTO;
import com.prix.user.Entity.SoftwareMsgEntity;

import java.util.List;

public interface SoftwareMsgService {
    String saveSoftwareMsg(SoftwareMsgEntity softwareMsgEntity);

    List<SoftwareMsgDTO> getSoftwareMsgList();

    void updateSoftwareMsg(String modaMessage, String dbondMessage, String searchMessage, String signMessage);

    void updateModaMessage(String modaMessage);
    void updateDbondMessage(String dbondMessage);
    void updateSearchMessage(String searchMessage);
    void updateSignMessage(String signMessage);

    String returnModaMessage();
    String returnDbondMessage();
    String returnSearchMessage();
    String returnSignMessage();
}
