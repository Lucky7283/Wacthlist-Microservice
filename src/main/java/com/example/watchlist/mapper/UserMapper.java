package com.example.watchlist.mapper;

import com.example.watchlist.dto.UserDto;
import com.example.watchlist.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .isDeleted(entity.isDeleted())
                .build();
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setRole(dto.getRole());
        entity.setDeleted(dto.isDeleted());
        return entity;
    }
}
