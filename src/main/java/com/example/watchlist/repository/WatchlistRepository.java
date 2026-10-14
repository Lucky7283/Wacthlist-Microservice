package com.example.watchlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.watchlist.entity.WatchListEntity;
import com.example.watchlist.entity.WatchListStatus;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchListEntity, Long> {
    List<WatchListEntity> findByUserIdAndIsDeletedFalse(Long userId);

    List<WatchListEntity> findByAnimeIdAndIsDeletedFalse(Long animeId);

    Optional<WatchListEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<WatchListEntity> findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(Long animeId, Long userId);

    default Optional<WatchListEntity> findByAnimeIdAndUserIdAndIsDeletedFalse(Long animeId, Long userId) {
        return findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(animeId, userId);
    }

    List<WatchListEntity> findByUserIdAndStatusAndIsDeletedFalse(Long userId, WatchListStatus status);
    @org.springframework.data.jpa.repository.Modifying(flushAutomatically = true, clearAutomatically = true)
    @org.springframework.data.jpa.repository.Query("update WatchListEntity e set e.isDeleted = true where e.user.id = :userId and e.isDeleted = false")
    int softDeleteByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
