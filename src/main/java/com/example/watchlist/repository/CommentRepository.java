package com.example.watchlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.watchlist.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByAnimeIdAndIsDeletedFalse(Long animeId);

    List<CommentEntity> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<CommentEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<CommentEntity> findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(Long animeId, Long userId);

    default Optional<CommentEntity> findByAnimeIdAndUserIdAndIsDeletedFalse(Long animeId, Long userId) {
        return findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(animeId, userId);
    }
    @org.springframework.data.jpa.repository.Modifying(flushAutomatically = true, clearAutomatically = true)
    @org.springframework.data.jpa.repository.Query("update CommentEntity e set e.isDeleted = true where e.user.id = :userId and e.isDeleted = false")
    int softDeleteByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
