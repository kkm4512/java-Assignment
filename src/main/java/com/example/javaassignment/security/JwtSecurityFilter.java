package com.example.javaassignment.security;

import com.example.javaassignment.domain.user.constant.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.javaassignment.security.JwtManager.AUTHORIZATION_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jm;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, @NonNull HttpServletResponse httpResponse, @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = httpRequest.getHeader(AUTHORIZATION_HEADER);

        if (jwt != null) {
            try {
                Claims claims = jm.toClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                UserRole userRole = claims.get("userRole", UserRole.class);

                // 사용자 인증이 아직 설정되지 않았다면
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // AuthUser 객체를 생성
                    AuthUser authUser = new AuthUser(userId, email, nickname, userRole);

                    // JwtAuthenticationToken으로 인증 객체 생성
                    JwtAuthentication authenticationToken = new JwtAuthentication(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    // SecurityContextHolder에 인증 객체 설정 (사용자 인증 처리)
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        // 요청을 다음 필터로 넘김
        chain.doFilter(httpRequest, httpResponse);
    }
}

