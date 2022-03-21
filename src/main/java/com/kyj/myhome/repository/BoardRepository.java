package com.kyj.myhome.repository;

import com.kyj.myhome.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitle(String title);
    List<Board> findByTitleOrContent(String title, String content);

    // Containing을 쓰면 매개변수로 온 searchText가 포함된 모든 Title, Content를 반환
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

}
