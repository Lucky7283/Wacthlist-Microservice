package com.example.watchlist.service.Interfaces;

import com.example.watchlist.dto.WatchListDto;

public interface WatchlistService {
    void addWatchlist(WatchListDto watchlistDto);

    void removeWatchlist(Long id);

    void changeWatchlist(Long id, WatchListDto watchlistDto);
}