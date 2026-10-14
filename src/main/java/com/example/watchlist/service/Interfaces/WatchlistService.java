package com.example.watchlist.service.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.watchlist.dto.WatchListDto;
import com.example.watchlist.entity.WatchListStatus;

public interface WatchlistService {
    void addWatchlist(WatchListDto watchlistDto);

    void removeWatchlist(Long id);

    void changeWatchlist(Long id, WatchListDto watchlistDto);

    Optional<WatchListDto> getWatchlistById(Long id);

    List<WatchListDto> getWatchlistByUserId(Long userId);

    List<WatchListDto> getWatchlistByAnimeId(Long animeId);

    Optional<WatchListDto> getWatchlistByAnimeIdAndUserId(Long animeId, Long userId);

    List<WatchListDto> getWatchlistByUserIdAndStatus(Long userId, WatchListStatus status);
}
