package com.example.watchlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.watchlist.dto.WatchListDto;
import com.example.watchlist.entity.WatchListStatus;
import com.example.watchlist.service.Interfaces.WatchlistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addWatchlist(@Valid @RequestBody WatchListDto watchlistDto) {
        watchlistService.addWatchlist(watchlistDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeWatchlist(@PathVariable Long id) {
        watchlistService.removeWatchlist(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> changeWatchlist(@PathVariable Long id, @Valid @RequestBody WatchListDto watchlistDto) {
        watchlistService.changeWatchlist(id, watchlistDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchListDto> getWatchlistById(@PathVariable Long id) {
        return watchlistService.getWatchlistById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchListDto>> getWatchlistByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) WatchListStatus status) {
        if (status != null) {
            return ResponseEntity.ok(watchlistService.getWatchlistByUserIdAndStatus(userId, status));
        }
        return ResponseEntity.ok(watchlistService.getWatchlistByUserId(userId));
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<WatchListDto>> getWatchlistByAnimeId(@PathVariable Long animeId) {
        return ResponseEntity.ok(watchlistService.getWatchlistByAnimeId(animeId));
    }

    @GetMapping("/anime/{animeId}/user/{userId}")
    public ResponseEntity<WatchListDto> getWatchlistByAnimeIdAndUserId(
            @PathVariable Long animeId,
            @PathVariable Long userId) {
        return watchlistService.getWatchlistByAnimeIdAndUserId(animeId, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
