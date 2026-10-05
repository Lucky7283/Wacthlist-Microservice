package com.example.watchlist.service;

import org.springframework.stereotype.Service;

import com.example.watchlist.dto.WatchListDto;
import com.example.watchlist.entity.WatchListEntity;
import com.example.watchlist.mapper.WatchListMapper;
import com.example.watchlist.repository.WatchlistRepository;
import com.example.watchlist.service.Interfaces.WatchlistService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final WatchListMapper watchListMapper;

    @Override
    public void addWatchlist(WatchListDto watchlistDto) {
        watchlistRepository.save(watchListMapper.toEntity(watchlistDto));
    }

    @Override
    public void removeWatchlist(Long id) {
        WatchListEntity entity = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with id: " + id));
        entity.setDeleted(true);
        watchlistRepository.save(entity);
    }

    @Override
    public void changeWatchlist(Long id, WatchListDto watchlistDto) {
        WatchListEntity entity = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with id: " + id));
        entity.setStatus(watchlistDto.getStatus());
        entity.setRating(watchlistDto.getRating());
        watchlistRepository.save(entity);
    }
}
