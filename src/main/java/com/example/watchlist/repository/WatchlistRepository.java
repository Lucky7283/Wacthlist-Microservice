package com.example.watchlist.repository;

import com.example.watchlist.entity.WatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface WatchlistRepository extends JpaRepository<WatchListEntity, Long> {
}