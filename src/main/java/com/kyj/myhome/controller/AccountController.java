package com.kyj.myhome.controller;

import com.kyj.myhome.model.User;
import com.kyj.myhome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    // 로그인 폼
    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String register() {
        return "account/register";
    }

    // 회원가입 진행
    @PostMapping("/register")
    public String register(User user) { // form에서 데이터를 파라미터에 받을 때 @ModelAttribute 생략
        userService.save(user);
        return "redirect:/";
    }
}
