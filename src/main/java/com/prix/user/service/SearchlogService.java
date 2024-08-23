package com.prix.user.service;

import com.prix.user.DTO.SearchlogDTO;
import com.prix.user.Entity.SearchlogEntity;

import java.util.List;

public interface SearchlogService {

    int saveSearchlog(SearchlogEntity searchlogEntity);

    SearchlogEntity findById(Long id);

    List<SearchlogDTO> getSearchlogList();
}
