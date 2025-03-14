package com.prix.user.service;

import com.prix.user.DTO.EnzymeDTO;
import com.prix.user.Entity.EnzymesEntity;
import com.prix.user.Repository.EnzymeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnzymeServiceImpl implements EnzymeService{
    private final EnzymeRepository enzymeRepository;

    public int saveEnzyme(EnzymesEntity enzymesEntity) {
        return enzymeRepository.save(enzymesEntity).getId();
    }

    @Override
    @Transactional
    public List<EnzymeDTO> getEnzymeList() {
        List<EnzymesEntity> enzymeList = enzymeRepository.findAll();
        List<EnzymeDTO> enzymeDTOList = new ArrayList<>();

        for(EnzymesEntity enzymesEntity : enzymeList) {
            EnzymeDTO enzymeDTO = EnzymeDTO.builder()
                    .id(enzymesEntity.getId())
                    .userID(enzymesEntity.getUserID())
                    .name(enzymesEntity.getName())
                    .ct_cleave(enzymesEntity.getCt_cleave())
                    .nt_cleave(enzymesEntity.getNt_cleave())
                    .build();
            enzymeDTOList.add(enzymeDTO);
        }
        return enzymeDTOList;
    }

    public void deleteEnzyme(int id) {
        enzymeRepository.deleteById(id);
    }

    @Override
    public void updateEnzyme(int id, String name, String ct_cleave, String nt_cleave) {
        EnzymesEntity enzymesEntity = enzymeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enzyme Id:" + id));
        enzymesEntity.setName(name);
        enzymesEntity.setCt_cleave(ct_cleave);
        enzymesEntity.setNt_cleave(nt_cleave);
        enzymeRepository.save(enzymesEntity);
    }
}
