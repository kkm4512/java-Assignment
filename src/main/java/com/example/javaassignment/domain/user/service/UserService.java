package com.example.javaassignment.domain.user.service;

import com.example.javaassignment.domain.user.constant.UserRole;
import com.example.javaassignment.domain.user.dto.request.SignRequestDto;
import com.example.javaassignment.domain.user.dto.request.SignupRequestDto;
import com.example.javaassignment.domain.user.dto.response.SignResponseDto;
import com.example.javaassignment.domain.user.dto.response.SignUpResponseDto;
import com.example.javaassignment.domain.user.entity.User;
import com.example.javaassignment.domain.user.repository.UserRepository;
import com.example.javaassignment.security.JwtDto;
import com.example.javaassignment.security.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pe;
    private final JwtManager jm;

    @Transactional
    public SignUpResponseDto signUp(SignupRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("중복된 회원 입니다");
        }

        String encodedPassword = pe.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .userRole(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
        return SignUpResponseDto.of(user);
    }



    @Transactional
    public SignResponseDto sign(SignRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("유저의 정보가 일치하지 않습니다");
        }

        String token = jm.generateJwt(JwtDto.of(user));
        return SignResponseDto.of(token);
    }
}
