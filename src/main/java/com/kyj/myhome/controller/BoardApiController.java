package com.kyj.myhome.controller;

import com.kyj.myhome.model.Board;
import com.kyj.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardApiController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "") String content) { // 검색기능 : @RequestParam은 url에 ?,&로 오는 것 (<->@Pathvariable은 /로 오는 것) // required를 false로 줬기에 title이 오지 않아도 에러 안남
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) { // title이 넘어오지 않았다면 검색기능을 수행한 것이 아니라 그냥 조회임
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/boards")
    Board newboard(@RequestBody Board newboard) {
        return boardRepository.save(newboard);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceboard(@RequestBody Board newboard, @PathVariable Long id) {
        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newboard.getTitle());
                    board.setContent(newboard.getContent());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newboard.setId(id);
                    return boardRepository.save(newboard);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteboard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
}
