package com.example.watchlist.dto;

import com.example.watchlist.entity.WatchListStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchListDto {
    private Long id;
    private Long userId;
    private Long animeId;
    private WatchListStatus status;
    private double rating;
    private boolean isDeleted;
}
