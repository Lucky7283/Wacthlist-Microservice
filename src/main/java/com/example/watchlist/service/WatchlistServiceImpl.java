package com.example.watchlist.service;

import com.example.watchlist.exception.ResourceNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.watchlist.dto.WatchListDto;
import com.example.watchlist.entity.WatchListEntity;
import com.example.watchlist.entity.WatchListStatus;
import com.example.watchlist.mapper.WatchListMapper;
import com.example.watchlist.repository.WatchlistRepository;
import com.example.watchlist.service.Interfaces.WatchlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final WatchListMapper watchListMapper;

    @Override
    @PreAuthorize("@accessPolicy.canAccessUser(#p0.userId)")
    public void addWatchlist(WatchListDto watchlistDto) {
        watchlistRepository.save(watchListMapper.toEntity(watchlistDto));
    }

    @Override
    @PreAuthorize("@accessPolicy.canAccessWatchlist(#p0)")
    public void removeWatchlist(Long id) {
        WatchListEntity entity = watchlistRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", id));
        entity.setDeleted(true);
        watchlistRepository.save(entity);
    }

    @Override
    @PreAuthorize("@accessPolicy.canAccessWatchlist(#p0)")
    public void changeWatchlist(Long id, WatchListDto watchlistDto) {
        WatchListEntity entity = watchlistRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist", id));
        if (!java.util.Objects.equals(entity.getUser().getId(), watchlistDto.getUserId())
                || !java.util.Objects.equals(entity.getAnimeId(), watchlistDto.getAnimeId())) {
            throw new com.example.watchlist.exception.InvalidRequestException("User and anime associations cannot be changed");
        }
        entity.setStatus(watchlistDto.getStatus());
        entity.setRating(watchlistDto.getRating());
        watchlistRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WatchListDto> getWatchlistById(Long id) {
        return watchlistRepository.findByIdAndIsDeletedFalse(id)
                .map(watchListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchListDto> getWatchlistByUserId(Long userId) {
        return watchlistRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(watchListMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchListDto> getWatchlistByAnimeId(Long animeId) {
        return watchlistRepository.findByAnimeIdAndIsDeletedFalse(animeId)
                .stream()
                .map(watchListMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WatchListDto> getWatchlistByAnimeIdAndUserId(Long animeId, Long userId) {
        return watchlistRepository.findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(animeId, userId)
                .map(watchListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchListDto> getWatchlistByUserIdAndStatus(Long userId, WatchListStatus status) {
        return watchlistRepository.findByUserIdAndStatusAndIsDeletedFalse(userId, status)
                .stream()
                .map(watchListMapper::toDto)
                .toList();
    }
}
