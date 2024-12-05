package com.example.javaassignment.domain.user.dto.response;

import com.example.javaassignment.domain.user.constant.UserRole;
import com.example.javaassignment.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {
    private String username;
    private String nickname;
    private List<Authority> authorities;

    @Getter // 이 부분 추가
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Authority {
        private UserRole authorityName;
    }

    public static SignUpResponseDto of(User user) {
        List<Authority> authorities = new ArrayList<>();
        Authority authority = new Authority(
                user.getUserRole()
        );
        authorities.add(authority);
        return new SignUpResponseDto(
                user.getUsername(),
                user.getNickname(),
                authorities
        );
    }
}
