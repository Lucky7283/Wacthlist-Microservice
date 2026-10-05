package com.example.watchlist.service;

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

    @Override
    public void addUser(UserDto userDto) {
        userRepository.save(UserMapper.toEntity(userDto));
    }

    @Override
    public void removeUser(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        entity.setDeleted(true);
        userRepository.save(entity);
    }

    @Override
    public void changeUser(Long id, UserDto userDto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        entity.setUsername(userDto.getUsername());
        entity.setRole(userDto.getRole());
        userRepository.save(entity);
    }
}
