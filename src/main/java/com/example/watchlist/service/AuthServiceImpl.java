package com.example.watchlist.service;

import java.time.Duration;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.watchlist.dto.AuthResponseDto;
import com.example.watchlist.dto.LoginRequestDto;
import com.example.watchlist.dto.RegisterRequestDto;
import com.example.watchlist.dto.UserDto;
import com.example.watchlist.entity.UserEntity;
import com.example.watchlist.entity.UserRoles;
import com.example.watchlist.exception.ResourceNotFoundException;
import com.example.watchlist.exception.UserAlreadyExistsException;
import com.example.watchlist.mapper.UserMapper;
import com.example.watchlist.repository.UserRepository;
import com.example.watchlist.security.JwtService;
import com.example.watchlist.service.Interfaces.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        UserEntity user = userRepository.findByUsernameAndIsDeletedFalse(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (loginRequest.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72
                || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.issue(user.getId());
        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.ttl().toSeconds())
                .user(UserMapper.toDto(user))
                .build();
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken: " + registerRequest.getUsername());
        }

        if (registerRequest.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new com.example.watchlist.exception.InvalidRequestException("Password must not exceed 72 UTF-8 bytes");
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRoles.USER);
        user.setDeleted(false);

        UserEntity savedUser = userRepository.save(user);
        String token = jwtService.issue(savedUser.getId());

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.ttl().toSeconds())
                .user(UserMapper.toDto(savedUser))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(Long userId) {
        UserEntity user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return UserMapper.toDto(user);
    }

    @Override
    public String createToken(Long userId) {
        getCurrentUser(userId);
        return jwtService.issue(userId);
    }

    @Override
    public boolean matchToken(String token) {
        if (!jwtService.matches(token)) {
            return false;
        }
        return userRepository.findByIdAndIsDeletedFalse(jwtService.userId(token)).isPresent();
    }

    @Override
    public boolean matchToken(String token, Long userId) {
        return jwtService.matches(token, userId) && matchToken(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Long userId = jwtService.userId(token);
        getCurrentUser(userId);
        return userId;
    }

    @Override
    public Duration getTokenTtl() {
        return jwtService.ttl();
    }
}
