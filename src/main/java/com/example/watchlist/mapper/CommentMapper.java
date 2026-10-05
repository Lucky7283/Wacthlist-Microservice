package com.example.watchlist.mapper;

import com.example.watchlist.dto.CommentDto;
import com.example.watchlist.entity.CommentEntity;
import com.example.watchlist.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public static CommentDto toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        return CommentDto.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .animeId(entity.getAnimeId())
                .isDeleted(entity.isDeleted())
                .build();
    }

    public static CommentEntity toEntity(CommentDto dto, UserEntity user) {
        if (dto == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setId(dto.getId());
        entity.setComment(dto.getComment());
        entity.setUser(user);
        entity.setAnimeId(dto.getAnimeId());
        entity.setDeleted(dto.isDeleted());

        return entity;
    }
}
