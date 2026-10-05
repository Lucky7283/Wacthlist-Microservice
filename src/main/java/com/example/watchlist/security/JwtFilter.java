package com.example.watchlist.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.watchlist.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies())
                if (c.getName().equals("jwt-token")) {
                    Long id = null;
                    try {
                        id = jwtService.userId(c.getValue());
                    } catch (org.springframework.security.oauth2.jwt.JwtException | IllegalArgumentException ignored) {
                    }
                    if (id != null)
                        userRepository.findById(id)
                                .ifPresent(u -> SecurityContextHolder.getContext()
                                        .setAuthentication(new UsernamePasswordAuthenticationToken(u.getId(), null,
                                                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole())))));
                    break;
                }
        }
        filterChain.doFilter(request, response);
    }
}
