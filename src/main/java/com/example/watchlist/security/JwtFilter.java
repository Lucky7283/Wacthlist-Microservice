package com.example.watchlist.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.watchlist.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Long id = null;
            try {
                id = jwtService.userId(token);
            } catch (org.springframework.security.oauth2.jwt.JwtException | IllegalArgumentException ignored) {
            }
            if (id != null) {
                userRepository.findByIdAndIsDeletedFalse(id)
                        .ifPresent(u -> SecurityContextHolder.getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(u.getId(), null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole())))));
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            if (authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
                return authHeader.substring(7).trim();
            }
            return "";
        }
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("jwt-token".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
