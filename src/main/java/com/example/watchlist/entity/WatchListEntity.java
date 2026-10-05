package com.example.watchlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "watchlist")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WatchListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "anime_id")
    private Long animeId;
    @Enumerated(EnumType.STRING)
    private WatchListStatus status;
    private double rating;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
