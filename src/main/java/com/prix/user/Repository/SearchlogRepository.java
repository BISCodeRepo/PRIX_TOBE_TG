package com.prix.user.Repository;

import com.prix.user.Entity.SearchlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
public interface SearchlogRepository extends JpaRepository<SearchlogEntity, Integer> {
    List<SearchlogEntity> findByUserId(Integer userId);

    Optional<SearchlogEntity> findByResult(String index);

    // SearchlogEntity에 데이터 저장
    @Modifying
    @Query("INSERT INTO SearchlogEntity (userId, title, date, msfile, db, result, engine) VALUES (:user_id, :title, CURRENT_DATE, :msfile, :db, :result, :engine)")
    void insert(@Param("user_id") Integer user_id, @Param("title") String title, @Param("msfile") Integer msfile, @Param("db") Integer db, @Param("result") String result, @Param("engine") String engine);

    // 데이터베이스에서 SearchlogEntity가 존재하지 않을 경우를 대비해 Optional 처리
    @Query("SELECT s FROM SearchlogEntity s WHERE s.result = :result")
    Optional<SearchlogEntity> getSearchLog(@Param("result") String result);

    // result(index) 값을 입력받아 id 출력
    @Query("SELECT s.userId FROM SearchlogEntity s WHERE s.result = :result")
    Integer findUserIDByResult(@Param("result") String result);

    // result(index) 값을 입력받아 title 출력
    @Query("SELECT s.title FROM SearchlogEntity s WHERE s.result = :result")
    String findTitleByResult(@Param("result") String result);

    // result(index) 값을 입력받아 date 출력
    @Query("SELECT s.date FROM SearchlogEntity s WHERE s.result = :result")
    LocalDate findDateByResult(@Param("result") String result);
}
