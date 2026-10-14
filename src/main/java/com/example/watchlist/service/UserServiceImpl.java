package com.example.watchlist.service;

import com.example.watchlist.exception.ResourceNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.watchlist.dto.UserDto;
import com.example.watchlist.entity.UserEntity;
import com.example.watchlist.mapper.UserMapper;
import com.example.watchlist.repository.UserRepository;
import com.example.watchlist.service.Interfaces.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.example.watchlist.repository.CommentRepository commentRepository;
    private final com.example.watchlist.repository.WatchlistRepository watchlistRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void addUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new com.example.watchlist.exception.UserAlreadyExistsException("Username is already taken: " + userDto.getUsername());
        }
        UserEntity entity = UserMapper.toEntity(userDto);
        if (entity.getPassword() == null || entity.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
        }
        userRepository.save(entity);
    }

    @Override
    @PreAuthorize("@accessPolicy.canAccessUser(#p0)")
    public void removeUser(Long id) {
        UserEntity entity = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        entity.setDeleted(true);
        userRepository.saveAndFlush(entity);
        commentRepository.softDeleteByUserId(id);
        watchlistRepository.softDeleteByUserId(id);
    }

    @Override
    @PreAuthorize("@accessPolicy.canEditUser(#p0, #p1.role)")
    public void changeUser(Long id, UserDto userDto) {
        UserEntity entity = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (userRepository.existsByUsernameAndIdNot(userDto.getUsername(), id)) {
            throw new com.example.watchlist.exception.UserAlreadyExistsException("Username is already taken: " + userDto.getUsername());
        }
        entity.setUsername(userDto.getUsername());
        entity.setRole(userDto.getRole());
        userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        return userRepository.findByIsDeletedFalse()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("@accessPolicy.canAccessUser(#p0)")
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .map(UserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("@accessPolicy.canAccessUsername(#p0)")
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .map(UserMapper::toDto);
    }
}
