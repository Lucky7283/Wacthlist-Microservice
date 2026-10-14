package com.example.watchlist.service.Interfaces;

import java.time.Duration;

import com.example.watchlist.dto.AuthResponseDto;
import com.example.watchlist.dto.LoginRequestDto;
import com.example.watchlist.dto.RegisterRequestDto;
import com.example.watchlist.dto.UserDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto loginRequest);

    AuthResponseDto register(RegisterRequestDto registerRequest);

    UserDto getCurrentUser(Long userId);

    String createToken(Long userId);

    boolean matchToken(String token);

    boolean matchToken(String token, Long userId);

    Long getUserIdFromToken(String token);

    Duration getTokenTtl();
}
