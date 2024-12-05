package com.example.javaassignment.security;

import com.example.javaassignment.domain.user.constant.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser implements Principal, Serializable {
    private final Long userId;
    private final String username;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String username, String nickname, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
