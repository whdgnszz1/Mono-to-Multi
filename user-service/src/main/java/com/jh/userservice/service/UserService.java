package com.jh.userservice.service;

import com.jh.userservice.dto.LoginRequestDto;
import com.jh.userservice.dto.SignupRequestDto;
import com.jh.userservice.entity.User;

public interface UserService {
    User signup(SignupRequestDto createUserRequestDto);

    String login(LoginRequestDto userCommonDto);
}