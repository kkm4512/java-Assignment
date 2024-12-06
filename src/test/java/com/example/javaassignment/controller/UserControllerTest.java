package com.example.javaassignment.controller;

import com.example.javaassignment.domain.user.dto.request.SignRequestDto;
import com.example.javaassignment.domain.user.dto.request.SignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 회원가입_컨트롤러_테스트() throws Exception {
        SignupRequestDto dto = new SignupRequestDto(
                "testUserName",
                "testPassword",
                "testNickname"
        );
        String stringDto = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType("application/json")
                        .content(stringDto)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 로그인_컨트롤러_테스트() throws Exception {
        회원가입_컨트롤러_테스트();
        SignRequestDto dto = new SignRequestDto(
                "testUserName",
                "testPassword"
        );
        String stringDto = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/v1/users/sign")
                        .contentType("application/json")
                        .content(stringDto)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
