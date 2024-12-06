package com.example.javaassignment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j(topic = "JwtManager")
@Component
public class JwtManager {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 24시간
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
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

    public String generateAccessToken(Map<String, Object> claims, String subject, Date expiration, String base64EncodedSecretKey) {
        Key keyFromBase64EncodedKey = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)          // JWT에 포함 시킬 Custom Claims를 추가함 (Custom Claims에는 주로 인증된 사용자와 관련된 정보를 추가)
                .setSubject(subject)        // JWT에 대한 제목을 추가
                .setIssuedAt(Calendar.getInstance().getTime())   // JWT 발행 일자를 설정, 파라미터 타입은 java.util.Date 타입
                .setExpiration(expiration)  // JWT의 만료일시를 지정
                .signWith(keyFromBase64EncodedKey)              // 서명을 위한 Key 객체를 설정
                .compact();                 // JWT를 생성하고 직렬화함
    }

    public String generateRefreshToken(JwtDto jwtDto) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key keyFromBase64EncodedKey = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(keyFromBase64EncodedKey)
                .compact();
    }

    public Claims toClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public void verifySignature(String jwt) {
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

    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key keyFromBase64EncodedKey = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(keyFromBase64EncodedKey) // 서명에 사용된 Secret Key를 설정
                .build()
                .parseClaimsJws(jws); // JWT를 파싱해서 Claims를 얻음
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
