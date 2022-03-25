package com.kyj.myhome.repository;

import com.kyj.myhome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = { "boards" }) // 쿼리에 user랑 board 조인 처리, fetch 타입들 전부 무시됨 = 여러개의 쿼리 발생X 하나의 조인으로 데이터 조회
    List<User> findAll();

    User findByUsername(String username);
}
