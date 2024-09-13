package com.prix.user.service;

import com.prix.user.DTO.SoftwareMsgDTO;
import com.prix.user.Entity.SoftwareMsgEntity;
import com.prix.user.Repository.SoftwareMsgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareMsgServiceImpl implements SoftwareMsgService{

    private final SoftwareMsgRepository softwareMsgRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updateModaMessage(String modaMessage) {
        String sql = "UPDATE px_software_msg SET message = ? WHERE id = 'moda'";
        jdbcTemplate.update(sql, modaMessage);
    }

    @Override
    public void updateDbondMessage(String dbondMessage) {
        String sql = "UPDATE px_software_msg SET message = ? WHERE id = 'dbond'";
        jdbcTemplate.update(sql, dbondMessage);
    }

    @Override
    public void updateSearchMessage(String searchMessage) {
        String sql = "UPDATE px_software_msg SET message = ? WHERE id = 'nextsearch'";
        jdbcTemplate.update(sql, searchMessage);
    }

    @Override
    public void updateSignMessage(String signMessage) {
        String sql = "UPDATE px_software_msg SET message = ? WHERE id = 'signature'";
        jdbcTemplate.update(sql, signMessage);
    }

    public String returnModaMessage(){
        String sql = "SELECT message FROM px_software_msg WHERE id = 'moda'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String returnDbondMessage(){
        String sql = "SELECT message FROM px_software_msg WHERE id = 'dbond'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String returnSearchMessage(){
        String sql = "SELECT message FROM px_software_msg WHERE id = 'nextsearch'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String returnSignMessage(){
        String sql = "SELECT message FROM px_software_msg WHERE id = 'signature'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String saveSoftwareMsg(SoftwareMsgEntity softwareMsgEntity) {
        return softwareMsgRepository.save(softwareMsgEntity).getId();
    }

    @Override
    @Transactional
    public List<SoftwareMsgDTO> getSoftwareMsgList() {
        List<SoftwareMsgEntity> softwareMsgList = softwareMsgRepository.findAll();
        List<SoftwareMsgDTO> SoftwareMsgDTOList = new ArrayList<>();

        for(SoftwareMsgEntity softwareMsgEntity : softwareMsgList) {
            SoftwareMsgDTO softwareMsgDTO = SoftwareMsgDTO.builder()
                    .id(softwareMsgEntity.getId())
                    .message(softwareMsgEntity.getMessage())
                    .build();
            SoftwareMsgDTOList.add(softwareMsgDTO);
        }
        return SoftwareMsgDTOList;
    }

    @Override
    @Transactional
    public void updateSoftwareMsg(String modaMessage, String dbondMessage, String searchMessage, String signMessage) {
        // 각각의 메시지를 데이터베이스에 업데이트하는 로직
        updateModaMessage(modaMessage);
        updateDbondMessage(dbondMessage);
        updateSearchMessage(searchMessage);
        updateSignMessage(signMessage);
    }
}
