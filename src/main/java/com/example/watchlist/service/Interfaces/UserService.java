package com.example.watchlist.service.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.watchlist.dto.UserDto;

public interface UserService {
    void addUser(UserDto userDto);

    void removeUser(Long id);

    void changeUser(Long id, UserDto userDto);

    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(Long id);

    Optional<UserDto> getUserByUsername(String username);
}
