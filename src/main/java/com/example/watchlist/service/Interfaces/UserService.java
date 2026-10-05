package com.example.watchlist.service.Interfaces;

import com.example.watchlist.dto.UserDto;

public interface UserService {
    void addUser(UserDto userDto);

    void removeUser(Long id);

    void changeUser(Long id, UserDto userDto);
}
