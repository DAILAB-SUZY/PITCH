package org.cosmic.backend.domain.user.controller;

import org.cosmic.backend.domain.user.dto.JoinRequest;
import org.cosmic.backend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //사용자로부터 회원가입 요청 받음
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody JoinRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("데이터 수신못함");
        }
        try {
            userService.registerUser(request);
            return ResponseEntity.ok("성공");//회원가입 완료 표시
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
