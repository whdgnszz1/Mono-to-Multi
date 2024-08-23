package com.jh.userservice.controller;

import com.jh.common.dto.ResponseMessage;
import com.jh.userservice.dto.LoginRequestDto;
import com.jh.userservice.dto.SignupRequestDto;
import com.jh.userservice.entity.User;
import com.jh.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody SignupRequestDto signupRequestDto) throws BadRequestException {
        User createdUser = userService.signup(signupRequestDto);

        ResponseMessage response = ResponseMessage.builder()
                .data(createdUser)
                .statusCode(201)
                .resultMessage("회원가입 성공")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) throws BadRequestException {
        String token = userService.login(loginRequestDto);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .data(token)
                .statusCode(200)
                .resultMessage("Login successful")
                .build();

        return ResponseEntity.ok(responseMessage);
    }
}
