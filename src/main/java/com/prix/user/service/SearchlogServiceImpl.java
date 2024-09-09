package com.prix.user.service;

import com.prix.user.Entity.SearchlogEntity;
import com.prix.user.DTO.SearchlogDTO;

import java.util.ArrayList;
import java.util.List;
import com.prix.user.Repository.SearchlogRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchlogServiceImpl implements SearchlogService{

    private final SearchlogRepository searchlogRepository;

    @Transactional
    public int saveSearchlog(SearchlogEntity searchlogEntity) {
        return searchlogRepository.save(searchlogEntity).getId();
    }

    @Override
    public SearchlogEntity findById(Long id) {
        return searchlogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find SearchlogEntity with id: " + id));
    }

    @Override
    @Transactional
    public List<SearchlogDTO> getSearchlogList() {
        List<SearchlogEntity> searchlogList = searchlogRepository.findAll();
        List<SearchlogDTO> searchlogDtoList = new ArrayList<>();

        for(SearchlogEntity searchlogEntity : searchlogList) {
            SearchlogDTO searchlogDTO = SearchlogDTO.builder()
                    .userID(searchlogEntity.getUserID())
                    .title(searchlogEntity.getTitle())
                    .date(searchlogEntity.getDate())
                    .msdata(searchlogEntity.getMsdata())
                    .db(searchlogEntity.getDb())
                    .result(searchlogEntity.getResult())
                    .engine(searchlogEntity.getEngine())
                    .build();
            searchlogDtoList.add(searchlogDTO);
        }
        return searchlogDtoList;
    }

    @Override
    @Transactional
    public List<SearchlogDTO> findByUserID(String userID) {
        List<SearchlogEntity> searchlogList = searchlogRepository.findByUserID(userID);
        List<SearchlogDTO> searchlogDtoList = new ArrayList<>();

        for(SearchlogEntity searchlogEntity : searchlogList) {
            SearchlogDTO searchlogDTO = SearchlogDTO.builder()
                    .userID(searchlogEntity.getUserID())
                    .title(searchlogEntity.getTitle())
                    .date(searchlogEntity.getDate())
                    .msdata(searchlogEntity.getMsdata())
                    .db(searchlogEntity.getDb())
                    .result(searchlogEntity.getResult())
                    .engine(searchlogEntity.getEngine())
                    .build();
            searchlogDtoList.add(searchlogDTO);
        }
        return searchlogDtoList;
    }
}
