package com.prix.user.service;

import com.prix.livesearch.DTO.ActgResultDTO;
import com.prix.livesearch.mapper.ActgSearchlogRepository;
import com.prix.user.Entity.SearchlogEntity;
import com.prix.user.DTO.SearchlogDTO;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.prix.user.Entity.UserEntity;
import com.prix.user.Repository.SearchlogRepository;

import com.prix.user.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchlogServiceImpl implements SearchlogService{

    private final SearchlogRepository searchlogRepository;
    private final UserRepository userRepository;
    private final ActgSearchlogRepository actgSearchlogRepository;

    @Transactional
    public int saveSearchlog(SearchlogEntity searchlogEntity) {
        return searchlogRepository.save(searchlogEntity).getId();
    }

    @Override
    public SearchlogEntity findById(Integer id) {
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
    public List<SearchlogDTO> findByUserID(Integer userID) {
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

    public Integer getUserIDByResult(String result) {
        return searchlogRepository.findUserIDByResult(result);
    }

    public LocalDate getDateByResult(String result) {
        return searchlogRepository.findDateByResult(result);
    }

    public String getTitleByResult(String result) {
        return searchlogRepository.findTitleByResult(result);
    }
}
