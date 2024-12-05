package com.example.javaassignment.security;

import com.example.javaassignment.domain.user.constant.UserRole;
import com.example.javaassignment.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private Long userId;
    private String nickname;
    private String username;
    private UserRole userRole;

    public static JwtDto of (User user) {
        return new JwtDto(
                user.getId(),
                user.getNickname(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
