package com.example.watchlist.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.watchlist.dto.AuthResponseDto;
import com.example.watchlist.dto.LoginRequestDto;
import com.example.watchlist.dto.RegisterRequestDto;
import com.example.watchlist.dto.UserDto;
import com.example.watchlist.service.Interfaces.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto registerRequest,
            HttpServletResponse response) {
        AuthResponseDto authResponse = authService.register(registerRequest);
        attachJwtCookie(response, authResponse.getToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest,
            HttpServletResponse response) {
        AuthResponseDto authResponse = authService.login(loginRequest);
        attachJwtCookie(response, authResponse.getToken());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(Authentication authentication) {
        Long userId = extractUserId(authentication);
        UserDto currentUser = authService.getCurrentUser(userId);
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long id) {
            return id;
        }
        if (principal instanceof Number num) {
            return num.longValue();
        }
        if (principal instanceof String str) {
            try {
                return Long.valueOf(str);
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
    }

    private void attachJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(authService.getTokenTtl())
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
