package com.prix.user.service;

import com.prix.user.DTO.EnzymeDTO;
import com.prix.user.Entity.EnzymesEntity;

import java.util.List;

public interface EnzymeService {
    int saveEnzyme(EnzymesEntity enzymesEntity);

    List<EnzymeDTO> getEnzymeList();

    void deleteEnzyme(int id);

    void updateEnzyme(int id, String name, String ct_cleave, String nt_cleave);
}
