package com.example.javaassignment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtManager")
@Component
public class JwtManager {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long ACCESS_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 24시간
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(JwtDto jwtDto) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .claim("email", jwtDto.getUsername())
                .claim("nickname", jwtDto.getNickname())
                .claim("userRole", jwtDto.getUserRole())
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String generateRefreshToken(JwtDto jwtDto) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .claim("email", jwtDto.getUsername())
                .claim("nickname", jwtDto.getNickname())
                .claim("userRole", jwtDto.getUserRole())
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public Claims toClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public void validateToken(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }
    }

}
