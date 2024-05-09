package org.cosmic.backend.domain.user.controller;

import jakarta.validation.Valid;
import org.cosmic.backend.domain.user.dto.JoinRequest;
import org.cosmic.backend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //사용자로부터 회원가입 요청 받음
    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody JoinRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }
}
