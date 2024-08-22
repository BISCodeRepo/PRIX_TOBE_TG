package com.prix.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseService {
    public String update(String value) {
        //TODO: 데이터베이스를 업데이트하는 로직 작성
        return value;
    }
}
