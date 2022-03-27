package com.kyj.myhome.controller;

import com.kyj.myhome.model.Board;
import com.kyj.myhome.model.QUser;
import com.kyj.myhome.model.User;
import com.kyj.myhome.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method, @RequestParam(required = false) String text) {
        Iterable<User> users = null;
//        log.debug("getBoards().size() 호출 전");
//        log.debug("getBoards().size() : {}", users.get(0).getBoards().size());
//        log.debug("getBoards().size() 호출 후");

        // 방법1
        if ("query".equals(method)) {
            users = userRepository.findByUsernameQuery(text);
        }
        // 방법2
        else if ("nativeQuery".equals(method)){
            users = userRepository.findByUsernameNativeQuery(text);
        }
        // 방법3
        else if ("querydsl".equals(method)) {
            QUser user = QUser.user;

            // BooleanExpression 사용
//            BooleanExpression b = user.username.contains(text);
//            if (true) {
//                b = b.and(user.username.eq("hi"));
//            }
//            users = userRepository.findAll(b);

            Predicate predicate = user.username.contains(text);
            users = userRepository.findAll(predicate);
        }
        // 방법4 - CustomizedUserRepositoryImpl 클래스에 정의
        else if ("querydslCustom".equals(method)) {
            users = userRepository.findByUsernameCustom(text);
        }
        // 방법5 - CustomizedUserRepositoryImpl 클래스에 정의
        else if ("jdbc".equals(method)) {
            users = userRepository.findByUsernameJdbc(text);
        }
        // 방법6
        else {
            users = userRepository.findAll();
        }
        return users;
    }

    @PostMapping("/users")
    User newuser(@RequestBody User newuser) {
        return userRepository.save(newuser);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceuser(@RequestBody User newuser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
//                    user.setBoards(newuser.getBoards());
                    user.getBoards().clear();
                    user.getBoards().addAll(newuser.getBoards());
                    for (Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newuser.setId(id);
                    return userRepository.save(newuser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteuser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
