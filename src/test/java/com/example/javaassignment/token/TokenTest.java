package com.example.javaassignment.token;

import com.example.javaassignment.domain.user.constant.UserRole;
import com.example.javaassignment.domain.user.entity.User;
import com.example.javaassignment.security.JwtDto;
import com.example.javaassignment.security.JwtManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TokenTest {
    @Autowired
    JwtManager jm;

    User user;

    @BeforeEach
    void init(){
        user = new User(
                1L,
                "test",
                "testName",
                "!@Skdud340",
                UserRole.ROLE_USER
        );
    }

    @Test
    public void 악세스_토큰_발행_테스트() {
        String token = jm.generateAccessToken(JwtDto.of(user));
        System.out.println(token);
        assertNotNull(token);
    }

    @Test
    public void 리프레쉬_토큰_발행_테스트() {
        String token = jm.generateRefreshToken(JwtDto.of(user));
        assertNotNull(token);
    }

    @Test
    public void 악세스_토큰_검증_테스트() {
        String token = jm.generateAccessToken(JwtDto.of(user));
        assertDoesNotThrow(() -> jm.verifySignature(token));
    }

    @Test
    public void 리프레쉬_토큰_검증_테스트() {
        String token = jm.generateRefreshToken(JwtDto.of(user));
        assertDoesNotThrow(() -> jm.verifySignature(token));
    }



}
