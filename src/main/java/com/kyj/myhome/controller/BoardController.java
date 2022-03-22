package com.kyj.myhome.controller;

import com.kyj.myhome.model.Board;
import com.kyj.myhome.repository.BoardRepository;
import com.kyj.myhome.service.BoardService;
import com.kyj.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size=3) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        // boards.getTotalElements() -> list 총 건수에 사용

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form")
    public String formsubmit(@Valid Board board, BindingResult bindingResult, Authentication authentication) { // 원래는 @Valid 전에 @ModelAttribute
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 파라미터 외에 authentication 가져오는 법
        String username = authentication.getName();
        boardService.save(username, board);
        return "redirect:/board/list";
    }

//    @PostMapping("/form")
//    public String formsubmit(@Valid Board board, BindingResult bindingResult, Principal principal) { // 원래는 @Valid 전에 @ModelAttribute
//        boardValidator.validate(board, bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "board/form";
//        }
//        String username = principal.getName();
//        boardService.save(username, board);
//        return "redirect:/board/list";
//    }

}
