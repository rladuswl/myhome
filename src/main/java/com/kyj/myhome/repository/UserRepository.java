package com.kyj.myhome.repository;

import com.kyj.myhome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, CustomizedUserRepository {

    @EntityGraph(attributePaths = { "boards" }) // 쿼리에 user랑 board 조인 처리, fetch 타입들 전부 무시됨 = 여러개의 쿼리 발생X 하나의 조인으로 데이터 조회
    List<User> findAll();

    User findByUsername(String username);

    // JPQL
    @Query("select u from User u where u.username like %?1%") // like는 user만 검색해도 user1, user2, user3 까지 전부 찾음
    List<User> findByUsernameQuery(String username);

    // 순수 SQL 쿼리 (nativeQuery)
    @Query(value = "select * from User u where u.username like %?1%", nativeQuery = true)
    List<User> findByUsernameNativeQuery(String username);
}
