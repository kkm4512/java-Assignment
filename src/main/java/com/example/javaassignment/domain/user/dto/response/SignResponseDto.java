package com.example.javaassignment.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignResponseDto {
    private String token;

    public static SignResponseDto of(String token) {
        return new SignResponseDto(token);
    }
}
