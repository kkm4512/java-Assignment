package com.example.javaassignment.jwt;

import com.example.javaassignment.security.JwtManager;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTest {
    private JwtManager jm;
    private String secretKey;
    private String key;

    @BeforeAll
    public void init() {
        jm = new JwtManager();
        secretKey = "kevin1234123412341234123412341234";  // encoded "a2V2aW4xMjM0MTIzNDEyMzQxMjM0MTIzNDEyMzQxMjM0"
        key = jm.encodeBase64SecretKey(secretKey);
    }

    @Test
    @DisplayName("인코딩한 key를 디코딩했을때, 맨 처음 키인 sercretKey와 동인한지 확인")
    public void encodeBase64SecretKeyTest() {
        String decodedKey = new String(Base64.getDecoder().decode(key));
        assertEquals(secretKey, decodedKey);
    }

    @Test
    @DisplayName("악세스토큰이 정상적으로 생성되는지 테스트")
    public void generateAccessTokenTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jm.generateAccessToken(claims, subject, expiration, key);

        System.out.println("accessToken : " + accessToken);

        Assertions.assertNotNull(accessToken);
    }

    @Test
    @DisplayName("리프레쉬토큰이 정상적으로 생성되는지 테스트")
    public void generateRefreshTokenTest() {
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiration = calendar.getTime();

        String refreshToken = jm.generateRefreshToken(subject, expiration, key);

        System.out.println("refreshToken : " + refreshToken);

        Assertions.assertNotNull(refreshToken);
    }

    @DisplayName("jwt 토큰이 검증됐는지 확인")
    @Test
    public void verifySignatureTest() {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        assertDoesNotThrow(() -> jm.verifySignature(accessToken, key));
    }

    @DisplayName("만료기한이 지난 Jwt 검증")
    @Test
    public void verifyExpirationTest() throws InterruptedException {
        String accessToken = getAccessToken(Calendar.SECOND, 1);
        assertDoesNotThrow(() -> jm.verifySignature(accessToken, key));

        TimeUnit.MILLISECONDS.sleep(1500);

        assertThrows(Exception.class, () -> jm.verifySignature(accessToken, key));
    }

    // 악세스 토큰 가져오는 헬퍼 메서드
    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();
        return jm.generateAccessToken(claims, subject, expiration, key);
    }

}
