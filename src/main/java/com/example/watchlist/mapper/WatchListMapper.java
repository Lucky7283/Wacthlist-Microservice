package com.example.watchlist.mapper;

import com.example.watchlist.dto.WatchListDto;
import com.example.watchlist.entity.UserEntity;
import com.example.watchlist.entity.WatchListEntity;
import com.example.watchlist.repository.UserRepository;
import com.example.watchlist.repository.WatchlistRepository;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WatchListMapper {
    private final UserRepository userRepository;

    public WatchListDto toDto(WatchListEntity entity) {
        if (entity == null) {
            return null;
        }
        return WatchListDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .animeId(entity.getAnimeId())
                .status(entity.getStatus())
                .rating(entity.getRating())
                .isDeleted(entity.isDeleted())
                .build();
    }

    public WatchListEntity toEntity(WatchListDto dto) {
        if (dto == null) {
            return null;
        }
        WatchListEntity entity = new WatchListEntity();
        entity.setId(dto.getId());
        if (dto.getUserId() != null) {
            entity.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId())));
        }
        entity.setAnimeId(dto.getAnimeId());
        entity.setStatus(dto.getStatus());
        entity.setRating(dto.getRating());
        entity.setDeleted(dto.isDeleted());
        return entity;
    }

    public WatchListEntity toEntity(WatchListDto dto, UserEntity user) {
        if (dto == null) {
            return null;
        }
        WatchListEntity entity = new WatchListEntity();
        entity.setId(dto.getId());
        entity.setUser(user);
        entity.setAnimeId(dto.getAnimeId());
        entity.setStatus(dto.getStatus());
        entity.setRating(dto.getRating());
        entity.setDeleted(dto.isDeleted());
        return entity;
    }
}
