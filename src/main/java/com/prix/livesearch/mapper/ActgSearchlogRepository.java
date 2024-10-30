package com.prix.livesearch.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActgSearchlogRepository {

    @Insert("INSERT INTO actg_search_log (user_id, search_time, query) VALUES (#{userId}, #{searchTime}, #{query})")
    void insertSearchLog(String userId, String searchTime, String query);
}

