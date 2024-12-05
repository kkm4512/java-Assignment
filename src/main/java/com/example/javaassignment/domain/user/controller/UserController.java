package com.example.javaassignment.domain.user.controller;

import com.example.javaassignment.domain.user.dto.request.SignRequestDto;
import com.example.javaassignment.domain.user.dto.request.SignupRequestDto;
import com.example.javaassignment.domain.user.dto.response.SignResponseDto;
import com.example.javaassignment.domain.user.dto.response.SignUpResponseDto;
import com.example.javaassignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignupRequestDto dto){
        return userService.signUp(dto);
    }

    @PostMapping("/sign")
    public SignResponseDto sign(@RequestBody SignRequestDto dto){
        return userService.sign(dto);
    }
}
